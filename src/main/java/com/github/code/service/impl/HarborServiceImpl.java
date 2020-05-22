/**
 * Copyright (C), 2020-04-09
 * FileName: HarborServiceImpl
 * Author:   heyanbo
 * Date:     2020/4/9 15:01
 * Description:
 */
package com.github.code.service.impl;

import com.github.code.bean.harbor.HarborServer;
import com.github.code.utils.ActionReturnUtil;
import com.github.code.utils.harbor.HarborClient;
import com.github.code.utils.harbor.HarborHttpsClientUtil;

import java.util.Map;

/**
 * <功能简要> <br>
 * <>
 *
 * @Author heyanbo
 * @createTime 2020/4/9 15:01
 * @since
 */

public class HarborServiceImpl {

    public ActionReturnUtil deleteRepo(String harborHost, String repo, String tag) throws Exception {
        HarborServer harborServer = new HarborServer();
        String url = HarborClient.getHarborUrl(harborServer) + "/api/repositories/" + repo + "/tags/" + tag;
        Map<String, Object> headers = HarborClient.getAdminCookieHeader(harborServer);
        ActionReturnUtil response = HarborHttpsClientUtil.httpDoDelete(url, null, headers);
        return ActionReturnUtil.returnSuccess();

    }
}
