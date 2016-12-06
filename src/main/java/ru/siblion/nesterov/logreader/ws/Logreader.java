package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.util.Methods;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;
import java.util.Map;

/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "Logreader")
@Stateless
public class Logreader {

    @WebMethod(operationName = "getBlock")
    public String getBlock(@WebParam(name = "string") String string, @WebParam(name = "location") String location) {

        String filePath;
        String myServerLogFile = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\webl_server1.log";
        String realServerLogFile = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\ws_server1.log02354";
        String testLogFile = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\test.log";
        String domainsDirectory = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains";


        if (location.equals("webl_server1")) {
            filePath = myServerLogFile;
        }

        filePath = testLogFile;


        List<Integer> regExpPositions = Methods.getExpressionPositions(string, filePath);
        List<Integer> blockPositions = Methods.getExpressionPositions("####", filePath);

        StringBuilder block = new StringBuilder();
        Map<Integer, Integer> regExpBlocksPositions = Methods.getRegExpBlocksPositions(regExpPositions, blockPositions);
        System.out.println(regExpBlocksPositions);

        for (Map.Entry<Integer, Integer> entry : regExpBlocksPositions.entrySet()) {
            block.append((Methods._getBlock(filePath, entry.getKey(), entry.getValue())));
        }

        return block.toString();
    }



}
