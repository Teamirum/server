package server.global.auth.oauth2.model;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SocialTypeHandler extends BaseTypeHandler<SocialType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SocialType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getSocialName());
    }

    @Override
    public SocialType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String socialName = rs.getString(columnName);
        return SocialType.fromSocialName(socialName);
    }

    @Override
    public SocialType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String socialName = rs.getString(columnIndex);
        return SocialType.fromSocialName(socialName);
    }

    @Override
    public SocialType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String socialName = cs.getString(columnIndex);
        return SocialType.fromSocialName(socialName);
    }
}
