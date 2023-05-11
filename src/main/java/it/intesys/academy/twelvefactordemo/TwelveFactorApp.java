package it.intesys.academy.twelvefactordemo;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.AwsHostNameUtils;
import com.amazonaws.util.StringUtils;
import io.javalin.Javalin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

public class TwelveFactorApp {

    public static void main(String[] args) {

        var serverPort = Configs.getIntegerProperty("SERVER_PORT");
        Javalin app = Javalin.create()
                .start(serverPort);

        app.get("/", ctx -> {
            var customMessage = Configs.getStringProperty("CUSTOM_MESSAGE");

            ctx.result(customMessage);
        });

        app.post("/upload", ctx -> {
            AmazonS3 s3Client = getS3Client();

            String filename = ctx.queryParam("filename");
            String content = ctx.queryParam("content");

            if (!s3Client.doesBucketExistV2("mybucket")) {
                s3Client.createBucket("mybucket");
            }

            s3Client.putObject("mybucket", filename, new ByteArrayInputStream(content.getBytes()), new ObjectMetadata());

            ctx.result("File uploaded successfully!");

        });
    }

    private static AmazonS3 getS3Client() {
        String awsEndpoint = Configs.getStringProperty("AWS_ENDPOINT");
        String awsAccessKey = Configs.getStringProperty("AWS_ACCESS_KEY");
        String awsSecretKey = Configs.getStringProperty("AWS_SECRET_KEY");
        String awsRegion = Configs.getStringProperty("AWS_REGION");
        var awsCredentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
        var builder = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentials);

        if (!StringUtils.isNullOrEmpty(awsEndpoint)) {
            // endpoint set, using minio, changing configs
            builder.withPathStyleAccessEnabled(true)
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(
                                    awsEndpoint,
                                    AwsHostNameUtils.parseRegion(awsEndpoint, AmazonS3Client.S3_SERVICE_NAME)
                            )
                    );
        } else if (!StringUtils.isNullOrEmpty(awsRegion)) {
            // region set, using aws s3, changing configs
            builder.withRegion(Regions.fromName(awsRegion));
        }

        return builder.build();
    }

}
