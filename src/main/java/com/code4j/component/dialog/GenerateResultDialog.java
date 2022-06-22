package com.code4j.component.dialog;

import com.code4j.component.panel.CommonPanel;
import com.code4j.pojo.GenerateResultInfo;
import com.code4j.util.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 代码生成结果 对话框
 *
 * @author liu_wp
 * @date 2020/12/18
 * @see
 */
public class GenerateResultDialog extends BaseDialog {
    /**
     * @param parentComponent
     * @param title
     * @param generateResultInfos
     */
    public GenerateResultDialog(final Component parentComponent, String title, List<GenerateResultInfo> generateResultInfos) {
        super(parentComponent, title, true, generateResultInfos,true);
    }


    @Override
    protected Component content() {
        if (extObj != null) {
            List<GenerateResultInfo> generateResultInfos = (List<GenerateResultInfo>) extObj;
            CommonPanel commonPanel = new CommonPanel();
            Box box1 = Box.createVerticalBox();
            Box box2 = Box.createVerticalBox();
            Box box3 = Box.createVerticalBox();
            MatteBorder bottomBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray);
            for (final GenerateResultInfo generateResultInfo : generateResultInfos) {
                JLabel type = new JLabel(generateResultInfo.getGenerateType());
                CommonPanel c1 = new CommonPanel();
                c1.add(type);
                c1.setBorder(bottomBorder);
                box1.add(c1);
                CommonPanel c2 = new CommonPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel path = new JLabel(StringUtils.isBlank(generateResultInfo.getGeneratePath()) ? generateResultInfo.getGenerateStatus() : generateResultInfo.getGeneratePath());
                c2.add(path);
                c2.setBorder(bottomBorder);
                box2.add(c2);
                CommonPanel c3 = new CommonPanel();
                JLabel copy = new JLabel(" 复制");
                copy.setForeground(Color.BLUE);
                copy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                copy.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //单击选中
                        if (e.getClickCount() == 1) {
                            String clipboardStr = path.getText();
                            SystemUtil.setClipboardString(clipboardStr);
                        }
                    }
                });
                c3.add(copy);
                JLabel open = new JLabel(" 打开");
                open.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //单击选中
                        if (e.getClickCount() == 1) {
                            String pathText = path.getText();
                            SystemUtil.open(pathText);
                        }
                    }
                });
                open.setForeground(Color.BLUE);
                open.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                c3.add(open);
                c3.setBorder(bottomBorder);
                box3.add(c3);
            }
            commonPanel.addList(box1, box2, box3);
            return commonPanel;
        }
        return null;
    }

    @Override
    protected void okClick() {
        this.close();
    }
}
