package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.util.Methods;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "Logreader")
@Stateless
public class Logreader {

    @WebMethod(operationName = "getLogMessageList")
    public String getLogMessageList(@WebParam(name = "string") String string, @WebParam(name = "location") String location) {


        List<Integer> regExpPositions;
        List<Integer> blockPositions;
        StringBuilder block = new StringBuilder();
        Map<Integer, Integer> regExpBlocksPositions;
        for (String logFilePath : Methods.getLogFilePaths(location)) {
            regExpPositions = Methods.getExpressionPositions(string, logFilePath);
            blockPositions = Methods.getExpressionPositions("####", logFilePath);
            regExpBlocksPositions = Methods.getRegExpBlocksPositions(regExpPositions, blockPositions);
            System.out.println(regExpBlocksPositions);
            for (Map.Entry<Integer, Integer> entry : regExpBlocksPositions.entrySet()) {
                block.append((Methods.getBlock(logFilePath, entry.getKey(), entry.getValue())).substring(4));
            }
        }

        return block.toString();
    }



}
