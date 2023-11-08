package ru.garrowd.scheduleattendanceservice.configs;

import com.university.userservice.grpc.user.GroupServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GrpcConfig {
    private final GrpcPropertiesConfig grpcPropertiesConfig;

    /*@Bean
    public UserServiceGrpc.UserServiceBlockingStub userGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(grpcPropertiesConfig.getClient().get("users").getAddress())
                                                      .usePlaintext()
                                                      .build();
        return UserServiceGrpc.newBlockingStub(channel);
    }*/

    @Bean
    public GroupServiceGrpc.GroupServiceBlockingStub groupGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(grpcPropertiesConfig.getClient().get("users").getAddress())
                                                      .usePlaintext()
                                                      .build();
        return GroupServiceGrpc.newBlockingStub(channel);
    }
}
