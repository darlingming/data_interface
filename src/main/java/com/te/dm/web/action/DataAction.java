package com.te.dm.web.action;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Date;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.te.dm.web.bean.DataEntity;
import com.te.dm.web.bean.DataResultEntity;
import com.te.dm.web.common.DataCodeEnum;
import com.te.dm.web.dto.DataDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author DM
 * http://localhost:8080/services?action=getValueCount&user_token=0000&key=11
 */
public class DataAction extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(DataAction.class);
    private DataDao dataDao = new DataDao();//数据调用类

    @Override
    public void init() throws ServletException {

    }

    /**
     * 获取总数
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String key = request.getParameter("key");
        String user_token = request.getParameter("user_token");
        String serialNumber = request.getParameter("serialNumber");

        logger.info("getValueCount>RemoteHost:" + request.getRemoteHost() +" getQueryString>"+ request.getQueryString());
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter pw = response.getWriter();
        DataResultEntity result = null;
        try {

            boolean token_bool = dataDao.checkToken(user_token);

            if (token_bool) {
                result = this.dataDao.getValueCount(key, serialNumber);
            } else {
                result = new DataResultEntity();
                result.setCode(DataCodeEnum.FAIL_SIGN.getCode());
                result.setMessage(DataCodeEnum.FAIL_SIGN.getMsg());
                result.setData(new DataEntity(null));
                result.setTime(new Date());
                result.setSerialNumber(serialNumber);
            }
            pw.println(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
            pw.flush();
            pw.close();

        } catch (Exception e) {
            logger.error("Exception>", e);
            result = new DataResultEntity();
            result.setCode(DataCodeEnum.ERROR.getCode());
            result.setMessage(DataCodeEnum.ERROR.getMsg());
            result.setData(new DataEntity(null));
            result.setTime(new Date());
            result.setSerialNumber(serialNumber);
            pw.println(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
            pw.flush();
            pw.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = request.getParameter("key");
        String user_token = request.getParameter("user_token");
        String serialNumber = request.getParameter("serialNumber");

        //logger.info("getValueCount>getRemoteHost:" + request.getRemoteHost() + "==" + key + "=" + user_token);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter pw = response.getWriter();
        DataResultEntity result = null;
        try {
            boolean token_bool = dataDao.checkToken(user_token);
            if (token_bool) {
                result = this.dataDao.getValueInfo(key, serialNumber);
            } else {
                result = new DataResultEntity();
                result.setCode(DataCodeEnum.FAIL_SIGN.getCode());
                result.setMessage(DataCodeEnum.FAIL_SIGN.getMsg());
                result.setData(new DataEntity(null));
                result.setTime(new Date());
                result.setSerialNumber(serialNumber);
            }
            pw.println(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
            pw.flush();
            pw.close();

        } catch (Exception e) {
            logger.error("Exception>", e);
            result = new DataResultEntity();
            result.setCode(DataCodeEnum.ERROR.getCode());
            result.setMessage(DataCodeEnum.ERROR.getMsg());
            result.setData(new DataEntity(null));
            result.setTime(new Date());
            result.setSerialNumber(serialNumber);
            pw.println(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
            pw.flush();
            pw.close();
        }
    }

    /**
     * 无服务时输出信息
     * @param response
     * @param serialNumber
     * @throws IOException
     */
    private void outmsg(HttpServletResponse response, String serialNumber) throws IOException {

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter pw = response.getWriter();
        DataResultEntity result = new DataResultEntity();
        result.setCode(DataCodeEnum.NOT_SERVICE.getCode());
        result.setMessage(DataCodeEnum.NOT_SERVICE.getMsg());
        result.setData(new DataEntity(null));
        result.setTime(new Date());
        result.setSerialNumber(serialNumber);
        pw.println(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
        pw.flush();
        pw.close();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //logger.info(request.getQueryString());
        String method = request.getMethod();
        String action = request.getParameter("action");
        String serialNumber = request.getParameter("serialNumber");
        if (method.equals("GET")) {
            if ("getValueCount".equals(action)) {//获取总数
                this.doGet(request, response);
            } else {
                this.outmsg(response, serialNumber);
            }

        } else if (method.equals("POST")) {
            if ("getValueInfo".equals(action)) {//获取数据信息
                this.doPost(request, response);
            } else {
                this.outmsg(response, serialNumber);
            }
        } else {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter pw = response.getWriter();
            DataResultEntity result = new DataResultEntity();
            result.setCode(DataCodeEnum.NOT_PROTOCOL_TYPE.getCode());
            result.setMessage(DataCodeEnum.NOT_PROTOCOL_TYPE.getMsg());
            result.setData(new DataEntity(null));
            result.setTime(new Date());
            result.setSerialNumber(serialNumber);
            pw.println(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
            pw.flush();
            pw.close();
        }


    }
}
