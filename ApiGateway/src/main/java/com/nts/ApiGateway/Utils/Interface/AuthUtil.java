package com.nts.ApiGateway.Utils.Interface;

import java.util.Map;

public interface AuthUtil {
    public Map<String, Object> validateAndExtract(String token);
}
