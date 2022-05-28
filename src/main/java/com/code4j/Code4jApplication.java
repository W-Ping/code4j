package com.code4j;

import com.code4j.component.Code4jMainFrom;
import com.code4j.component.panel.BottomPanel;
import com.code4j.component.panel.LeftPanel;
import com.code4j.component.panel.RightPanel;
import com.code4j.component.panel.TopPanel;
import com.code4j.config.Code4jConstants;
import com.code4j.util.SQLiteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class Code4jApplication {
    private static final Logger log = LoggerFactory.getLogger(Code4jApplication.class);

    public static void main(String[] args) throws Exception {
        log.info("---------------------init SQLite ---------------------------");
        if (SQLiteUtil.init()) {
            log.info("---------------------init SQLite Success -------------------");
            RightPanel rightPanel = new RightPanel(Code4jConstants.RIGHT_MIN_DEFAULT_SIZE);
            LeftPanel leftPanel = new LeftPanel(Code4jConstants.LEFT_MIN_DEFAULT_SIZE, rightPanel);
            TopPanel topPanel = new TopPanel(Code4jConstants.TOP_MIN_DEFAULT_SIZE, leftPanel);
            BottomPanel bottomPanel = new BottomPanel(Code4jConstants.BOTTOM_MIN_DEFAULT_SIZE);
            Code4jMainFrom.Builder()
                    .title("代码生成工具")
                    .icon(Code4jConstants.SYS_ICON)
                    .topPanel(topPanel)
                    .leftPanel(leftPanel)
                    .rightPanel(rightPanel)
                    .bottomPanel(bottomPanel)
                    .build();
            log.info("---------------------Run code4j Success --------------------");
        } else {
            log.error("---------------------init SQLite Fail---------------------------");
            log.error("---------------------Run code4j Fail -----------------------");
        }

    }
}
