package cn.edu.ldu.sniffer;

import cn.edu.ldu.sniffer.file.FileExporter;
import cn.edu.ldu.sniffer.file.FileImporter;
import cn.edu.ldu.sniffer.file.MalformedDataException;
import cn.edu.ldu.sniffer.filter.PacketList;
import cn.edu.ldu.sniffer.gui.InterfaceWindow;
import cn.edu.ldu.sniffer.gui.PacketDetail;
import cn.edu.ldu.sniffer.gui.PacketTable;
import cn.edu.ldu.sniffer.gui.PacketTree;
import cn.edu.ldu.sniffer.packet.MyPacket;
import cn.edu.ldu.sniffer.packet.MyPacketCapture;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

//抓包主程序
public class Main extends javax.swing.JFrame {
    private InetAddress address;
    private PacketDetail packetDetail;//包明细
    private JFileChooser fileChooser=null;//文件对话框
    private MyPacketCapture packetCapture;//抓包类
    private PacketTable packetTable; //显示数据包列表
    private PacketTree packetTree; //显示包明细
    private Thread captureThread; //抓包线程
    private FileImporter fileImporter = new FileImporter(); //打开文件
    private FileExporter fileExporter = new FileExporter(); //保存文件
    public static int order=0; //当前包的序号
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        //设定文件默认目录
        fileChooser = new JFileChooser();
        if (System.getProperty("user.home") != null && !System.getProperty("user.home").isEmpty()) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        }
        packetTable = new PacketTable(this);//初始化表格
        packetDetail = new PacketDetail(null);//初始化包明细
        packetTree=new PacketTree(this,null);//初始化包明细显示树和文本框
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnOpenFile = new javax.swing.JButton();
        btnSaveFile = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnStart = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        packetBytes = new javax.swing.JTextArea();
        lblStatus = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mFile = new javax.swing.JMenu();
        openFile = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        saveFile = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        quit = new javax.swing.JMenuItem();
        mCapturing = new javax.swing.JMenu();
        setInterface = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        start = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        stop = new javax.swing.JMenuItem();
        mHelp = new javax.swing.JMenu();
        about = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("网络嗅探器--设计：董相志，@版权所有，2017--2018，upsunny2008@163.com");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnOpenFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open.png"))); // NOI18N
        btnOpenFile.setText("打开文件");
        btnOpenFile.setToolTipText("打开文件");
        btnOpenFile.setFocusable(false);
        btnOpenFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenFileActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenFile);

        btnSaveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        btnSaveFile.setText("保存文件");
        btnSaveFile.setToolTipText("保存文件");
        btnSaveFile.setFocusable(false);
        btnSaveFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFileActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveFile);
        jToolBar1.add(jSeparator3);

        btnStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/start.png"))); // NOI18N
        btnStart.setText("开始抓包");
        btnStart.setToolTipText("开始抓包");
        btnStart.setFocusable(false);
        btnStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        jToolBar1.add(btnStart);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stop.png"))); // NOI18N
        btnStop.setText("停止抓包");
        btnStop.setToolTipText("停止抓包");
        btnStop.setEnabled(false);
        btnStop.setFocusable(false);
        btnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        jToolBar1.add(btnStop);

        jLabel1.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        jLabel1.setText(" 过滤器：");

        txtFilter.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        txtFilter.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtFilter.setDragEnabled(true);
        txtFilter.setPreferredSize(new java.awt.Dimension(100, 21));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar1.add(jPanel1);

        btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/filter.png"))); // NOI18N
        btnFilter.setToolTipText("应用");
        btnFilter.setFocusable(false);
        btnFilter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFilter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFilter);

        table.setAutoCreateRowSorter(true);
        table.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "编号", "时间序列", "源地址", "源端口", "目标地址", "目标端口", "协议", "长度"
            }
        ));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        jScrollPane1.setViewportView(table);

        tree.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("分层协议");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Frame");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Ethernet II");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("IPV4");
        treeNode1.add(treeNode2);
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tree.setAutoscrolls(true);
        tree.setLargeModel(true);
        jScrollPane2.setViewportView(tree);

        packetBytes.setColumns(20);
        packetBytes.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        packetBytes.setRows(5);
        packetBytes.setText("以16进制字符显示包的详细信息");
        jScrollPane3.setViewportView(packetBytes);

        lblStatus.setText("欢迎您通过本软件学习抓包和协议分析......");

        jMenuBar1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N

        mFile.setText("文件");
        mFile.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N

        openFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        openFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open.png"))); // NOI18N
        openFile.setText("打开包文件...");
        openFile.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        openFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileActionPerformed(evt);
            }
        });
        mFile.add(openFile);
        mFile.add(jSeparator1);

        saveFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        saveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        saveFile.setText("保存包文件...");
        saveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileActionPerformed(evt);
            }
        });
        mFile.add(saveFile);
        mFile.add(jSeparator2);

        quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        quit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
        quit.setText("退出");
        quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitActionPerformed(evt);
            }
        });
        mFile.add(quit);

        jMenuBar1.add(mFile);

        mCapturing.setText("抓包");
        mCapturing.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N

        setInterface.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        setInterface.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/interface.png"))); // NOI18N
        setInterface.setText("网络接口...");
        setInterface.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setInterfaceActionPerformed(evt);
            }
        });
        mCapturing.add(setInterface);
        mCapturing.add(jSeparator5);

        start.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
        start.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/start.png"))); // NOI18N
        start.setText("开始抓包");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });
        mCapturing.add(start);
        mCapturing.add(jSeparator4);

        stop.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stop.png"))); // NOI18N
        stop.setText("停止抓包");
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });
        mCapturing.add(stop);

        jMenuBar1.add(mCapturing);

        mHelp.setText("帮助");
        mHelp.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N

        about.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about.png"))); // NOI18N
        about.setText("关于...");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        mHelp.add(about);

        jMenuBar1.add(mHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 831, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    //设置抓包地址
    public void updateAddress(InetAddress address) {
        this.address = address;
    }
    //在树形窗口和bytes窗口显示选定包的详细信息
    public void analyzingPacket(MyPacket packet) {
        if (packetDetail.packet != packet) {
            packetDetail = new PacketDetail(packet);
            packetTree=new PacketTree(this,packetDetail); 
        }
    } 
    //退出程序
    private void quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitActionPerformed
        if (captureThread != null) {
            captureThread.interrupt();
        }
    }//GEN-LAST:event_quitActionPerformed
    //关闭程序之前
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (captureThread != null) {
            captureThread.interrupt();
        }
    }//GEN-LAST:event_formWindowClosing
    //工具栏：开始抓包
    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        lblStatus.setText("正在捕获数据包，请耐心等候...");
        btnStart.setEnabled(false);
        order=0;
         DefaultTableModel tableModel = (DefaultTableModel)table.getModel();            
        while(tableModel.getRowCount()>0){
            tableModel.removeRow(tableModel.getRowCount()-1);
        }
        table.setModel(tableModel); 
        packetTable.resetFilter();
        packetDetail = new PacketDetail(null);
        packetTree=new PacketTree(this,null); 
        if (address == null) {
            showMessage("请先指定网卡地址！");
            btnStart.setEnabled(true);
            return;
        }
        if (packetCapture == null) {
            try {
                packetCapture = new MyPacketCapture(address);
                packetCapture.registerListener((packet) -> packetTable.addPacket(packet));
            } catch (PcapNativeException ex) {
                showError(ex);
            }
        }
        captureThread = new Thread(() -> {
            try {
                packetCapture.startCapturing();
            } catch (PcapNativeException | NotOpenException | InterruptedException ex) {
                showError(ex);
            }
        });
        captureThread.start();
        btnStop.setEnabled(true);
    }//GEN-LAST:event_btnStartActionPerformed
    //工具栏：停止抓包
    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        btnStop.setEnabled(false);
        try {
            packetCapture.stopCapturing();
            captureThread.interrupt();
            captureThread = null;
        } catch (NotOpenException ex) {
            showError(ex);
        }
        packetCapture=null;       
        btnStart.setEnabled(true); 
        lblStatus.setText("当前捕获数据包工作已结束！");
    }//GEN-LAST:event_btnStopActionPerformed
    //菜单项：选择网络接口
    private void setInterfaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setInterfaceActionPerformed
        lblStatus.setText("请选择希望抓包的网络接口...");
        showInterfaceWindow();
    }//GEN-LAST:event_setInterfaceActionPerformed
    //菜单项：保存文件
    private void saveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileActionPerformed
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                fileExporter.exportToFile(file.getAbsolutePath(), packetTable.getFilterPackets());
            } catch (IOException e) {
                showError(e);
            }
        }
        lblStatus.setText("文件保存完成！");
    }//GEN-LAST:event_saveFileActionPerformed
    //菜单项：打开文件
    private void openFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileActionPerformed
        order=0;
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                PacketList packets = fileImporter.readFromFile(file.getAbsolutePath());
                packetTable.setPacketList(packets);
            } catch (IOException | MalformedDataException e) {
                showError(e);
            }
        }
        lblStatus.setText("文件已打开！");
    }//GEN-LAST:event_openFileActionPerformed
    //工具栏：打开文件
    private void btnOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenFileActionPerformed
        openFileActionPerformed(evt);
    }//GEN-LAST:event_btnOpenFileActionPerformed
    //工具栏：保存文件
    private void btnSaveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFileActionPerformed
        saveFileActionPerformed(evt);
    }//GEN-LAST:event_btnSaveFileActionPerformed
    //菜单项：开始抓包
    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        btnStartActionPerformed(evt);
    }//GEN-LAST:event_startActionPerformed
    //菜单项：停止抓包
    private void stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopActionPerformed
        btnStopActionPerformed(evt);
    }//GEN-LAST:event_stopActionPerformed
    //设置过滤规则生效
    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        packetTable.filterPacketList(txtFilter.getText());
        lblStatus.setText("过滤器规则生效...");
    }//GEN-LAST:event_btnFilterActionPerformed
    //关于
    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutActionPerformed
        JOptionPane.showMessageDialog(null, "设计：董相志\n@版权所有：2017-2018\nEmail:upsunny2008@163.com\n", "关于Sniffer...", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutActionPerformed
    //显示网络接口列表
    private void showInterfaceWindow() {
        try {
            JFrame nifWindow = new InterfaceWindow(this, address);
            nifWindow.setLocationRelativeTo(this);
            nifWindow.setVisible(true);
        } catch (SocketException e) {
            showError(e);
        }
    }
    //正常消息提示
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    //异常提示
    private void showError(Throwable e) {
        showMessage("错误消息：" + e.getClass().toString() + ": " + e.getMessage());
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem about;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnOpenFile;
    private javax.swing.JButton btnSaveFile;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JMenu mCapturing;
    private javax.swing.JMenu mFile;
    private javax.swing.JMenu mHelp;
    private javax.swing.JMenuItem openFile;
    public javax.swing.JTextArea packetBytes;
    private javax.swing.JMenuItem quit;
    private javax.swing.JMenuItem saveFile;
    private javax.swing.JMenuItem setInterface;
    private javax.swing.JMenuItem start;
    private javax.swing.JMenuItem stop;
    public javax.swing.JTable table;
    public javax.swing.JTree tree;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
}
