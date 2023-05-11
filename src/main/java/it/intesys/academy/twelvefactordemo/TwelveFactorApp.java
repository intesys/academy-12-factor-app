package it.intesys.academy.twelvefactordemo;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.AwsHostNameUtils;
import com.amazonaws.util.StringUtils;
import io.javalin.Javalin;

import java.io.ByteArrayInputStream;
import java.net.InetAddress;

public class TwelveFactorApp {

    public static void main(String[] args) {

        //create s3 client
        AmazonS3 s3Client = getS3Client();

        // initializing s3 bucket
        String bucketName = Configs.getStringProperty("AWS_BUCKET");
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
        }

        // start web server
        var serverPort = Configs.getIntegerProperty("SERVER_PORT");
        Javalin app = Javalin.create()
                .start(serverPort);

        // GET the home page of the app
        app.get("/", ctx -> {
            var customMessage = Configs.getStringProperty("CUSTOM_MESSAGE");

            ctx.result(customMessage + " (from " + InetAddress.getLocalHost().getHostName() + ")");
        });

        app.get("/files", ctx -> {
            var files = s3Client.listObjects(bucketName);
            ctx.json(files);
        });

        app.post("/files", ctx -> {

            var newFileRequest = ctx.bodyAsClass(NewFileRequest.class);
            String filename = newFileRequest.getFileName();
            String content = newFileRequest.getFileContent();

            if (content.length() > 20) {
                throw new RuntimeException("File content too long");
            }

            log.info("upload file " + filename + " with content " + content);

            PutObjectResult s3File = s3Client.putObject(
                    bucketName,
                    filename,
                    new ByteArrayInputStream(content.getBytes()),
                    new ObjectMetadata()
            );

            ctx.json(s3File);

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
