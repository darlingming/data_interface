package com.te.dm.web.dto;

import com.te.dm.web.bean.DataEntity;
import com.te.dm.web.bean.DataResultEntity;
import com.te.dm.web.common.DataCodeEnum;
import com.te.dm.web.common.JdbcConnectionsPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author  DM
 */
public class DataDao {
    private static final Logger logger = LogManager.getLogger(DataDao.class);

    /**
     *获取总数
     * @param key
     * @param serialNumber
     * @return
     */
    public DataResultEntity getValueCount(String key, String serialNumber) {
        DataResultEntity result = new DataResultEntity();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = JdbcConnectionsPool.getConnection();

            //3,用prepareStatement获取sql语句
            String sql = "SELECT t.data_value FROM t_data_count  t  where  t.data_key =  ?";

            //3,用prepareStatement获取sql语句
            ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            rs = ps.executeQuery();

            if (rs.next()) {
                result.setCode(DataCodeEnum.SUCCESS.getCode());
                result.setMessage(DataCodeEnum.SUCCESS.getMsg());
                result.setTime(new Date());
                result.setData(new DataEntity(rs.getString("data_value")));
                result.setSerialNumber(serialNumber);
            } else {
                result.setCode(DataCodeEnum.DATA_NOT_FOUND.getCode());
                result.setMessage(DataCodeEnum.DATA_NOT_FOUND.getMsg());
                result.setTime(new Date());
                result.setData(new DataEntity(null));
                result.setSerialNumber(serialNumber);
            }

            rs.close();
            ps.close();
            JdbcConnectionsPool.closeConnection();
        } catch (SQLException e) {
            logger.error("SQLException", e);
            result.setCode(DataCodeEnum.ERROR.getCode());
            result.setMessage(DataCodeEnum.ERROR.getMsg());
            result.setTime(new Date());
            result.setData(new DataEntity(null));
            result.setSerialNumber(serialNumber);
        }


        return result;


    }


    /**
     * 获取数据信息
     * @param key
     * @param serialNumber
     * @return
     */
    public DataResultEntity getValueInfo(String key, String serialNumber) {
        DataResultEntity result = new DataResultEntity();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = JdbcConnectionsPool.getConnection();

            //3,用prepareStatement获取sql语句
            String sql = "SELECT t.data_value FROM t_data_info t where t.data_key = ?";

            //3,用prepareStatement获取sql语句
            ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            rs = ps.executeQuery();

            if (rs.next()) {
                result.setCode(DataCodeEnum.SUCCESS.getCode());
                result.setMessage(DataCodeEnum.SUCCESS.getMsg());
                result.setTime(new Date());
                result.setData(new DataEntity(rs.getString("data_value")));
                result.setSerialNumber(serialNumber);
            } else {
                result.setCode(DataCodeEnum.DATA_NOT_FOUND.getCode());
                result.setMessage(DataCodeEnum.DATA_NOT_FOUND.getMsg());
                result.setTime(new Date());
                result.setData(new DataEntity(null));
                result.setSerialNumber(serialNumber);
            }

            rs.close();
            ps.close();
            JdbcConnectionsPool.closeConnection();
        } catch (SQLException e) {
            logger.error("SQLException", e);
            result.setCode(DataCodeEnum.ERROR.getCode());
            result.setMessage(DataCodeEnum.ERROR.getMsg());
            result.setTime(new Date());
            result.setData(new DataEntity(null));
            result.setSerialNumber(serialNumber);
        }


        return result;


    }

    /**
     * 校验token是否正确
     *
     * @param token
     * @return
     */
    public boolean checkToken(String token) throws SQLException {
        boolean bool = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = JdbcConnectionsPool.getConnection();
            //2,执行数据库语句
            String sql = "SELECT t.token FROM t_data_token t where t.token = ?";

            //3,用prepareStatement获取sql语句
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);

            rs = ps.executeQuery();

            if (rs.next()) {
                rs.getString("token");
                bool = true;
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            logger.error("SQLException", e);
            throw e;
        }

        return bool;
    }

}
