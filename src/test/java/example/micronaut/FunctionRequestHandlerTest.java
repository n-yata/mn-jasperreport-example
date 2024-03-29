package example.micronaut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class FunctionRequestHandlerTest {

    @Inject
    private FunctionRequestHandler handler;

    @BeforeEach
    void beforeEach() {
    }

    APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

    @Test
    public void testHandler() {
        APIGatewayProxyResponseEvent response = handler.execute(request);
        System.out.println(response.getStatusCode());
    }
}
