package com.code4j.util;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * future 接受外部文档
 *
 * @author lwp
 * @date 2022-03-24
 */
public class DragAndDrop extends JFrame implements DropTargetListener {
    private static final long serialVersionUID = 1L;
    JLabel cmdLabel = new JLabel("你拖入的文件地址是：");

    public DragAndDrop() {
        Container CP = getContentPane();
        CP.add(cmdLabel, BorderLayout.WEST);
        setSize(300, 60);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(200, 50);
        setTitle("读取命令行输入");
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        setVisible(true);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                java.util.List<?> list = (List<?>) (dtde.getTransferable()
                        .getTransferData(DataFlavor.javaFileListFlavor));
                Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    File f = (File) iterator.next();
                    this.cmdLabel.setText("你拖入的文件是：" + f.getAbsolutePath());
                }
                dtde.dropComplete(true);
                // this.updateUI();
            } else {
                dtde.rejectDrop();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public static void main(String[] args) {
        DragAndDrop c = new DragAndDrop();
        if (args.length != 0) {
            c.cmdLabel.setText(args[0]);
        }
    }

}
