package cn.edu.ldu.sniffer.gui;

import cn.edu.ldu.sniffer.Main;
import cn.edu.ldu.sniffer.packet.MyNetworkInterface;
import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

/**
 * 定义一个新窗体，以单选按钮组的形式显示所有可用的网络接口。用户选择一个接口，单击确定按钮后将地址更新至父类中的address。
 */
public class InterfaceWindow extends JFrame {
    private Main parent;
    private InetAddress selectedAddress;
    private Map<NetworkInterface, List<InetAddress>> addressMap;
    public InterfaceWindow(Main parent, InetAddress selectedAddress) throws SocketException {
        this.addressMap =  MyNetworkInterface.getIpv4AddressMap();
        this.selectedAddress = selectedAddress;
        this.parent = parent;
        setMinimumSize(new Dimension(400, 150));
        setTitle("选择希望抓包的网络接口");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        createAddressSelector();
        createSubmitButtons();
    }
   //生成用户列表
    private void createAddressSelector() {
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.PAGE_AXIS));
        ButtonGroup buttonGroup = new ButtonGroup();
        for (NetworkInterface nif : addressMap.keySet()) {
            for (InetAddress address : addressMap.get(nif)) {
                JPanel addressPanel = new JPanel();
                addressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.LINE_AXIS));
                addressPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                JRadioButton radioButton = new JRadioButton(nif.getDisplayName());
                if (address.equals(selectedAddress)) {
                    radioButton.setSelected(true);
                }
                radioButton.addActionListener((a) -> selectedAddress = address);
                buttonGroup.add(radioButton);
                addressPanel.add(radioButton);
                addressPanel.add(new JLabel(address.getHostAddress()));
                selectPanel.add(addressPanel);
            }
        }
        getContentPane().add(selectPanel, BorderLayout.NORTH);
    }
    //确定按钮将地址传送至父类并关闭窗口，取消则只是关闭窗口
    private void createSubmitButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton okBtn = new JButton("确定");
        okBtn.addActionListener((a) -> {
            parent.updateAddress(selectedAddress);
            dispose();
        });
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener((a) -> dispose());
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
