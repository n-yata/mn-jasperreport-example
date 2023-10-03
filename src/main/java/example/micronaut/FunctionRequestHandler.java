package example.micronaut;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.micronaut.domain.FuncRequest;
import example.micronaut.domain.FuncResponse;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class FunctionRequestHandler
        extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request) {

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            FuncRequest req = new FuncRequest();
            FuncResponse res = runProcess(req);

            response.setStatusCode(200);
            response.setBody(objectMapper.writeValueAsString(res));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            JSONObject obj = new JSONObject();
            obj.put("error", "error");
            response.setBody(obj.toString());
        }
        return response;
    }

    /**
     * メイン処理
     * 
     * @param request
     * @return
     * @throws JRException 
     */
    private FuncResponse runProcess(FuncRequest request) throws JRException {

        ClassPathResourceLoader loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).get();
        Optional<URL> resource = loader.getResource("classpath:templates/example.jrxml");

        String jasperFilePath = resource.get().getPath();

        Map<String, Object> parameterMap = new HashMap<>();
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperFilePath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jasperPrint, "jasper.pdf");

        FuncResponse response = new FuncResponse();
        response.setResult("OK");
        return response;
    }
}
