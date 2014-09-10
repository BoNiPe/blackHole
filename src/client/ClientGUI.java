package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import shared.Protocol;

public class ClientGUI extends javax.swing.JFrame implements EchoListener {

    Client currentClient;

    public ClientGUI() {
        initComponents();
        jButtonDisconnect.setEnabled(false);
        jButtonSend.setEnabled(false);
        jTextFieldSend.setEnabled(false);
        jTextPaneChat.setEnabled(false);
        jListOnline.setEnabled(false);
        jButtonNickname.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonDisconnect = new javax.swing.JButton();
        jButtonConnect = new javax.swing.JButton();
        jTextFieldSend = new javax.swing.JTextField();
        jButtonSend = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListOnline = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneChat = new javax.swing.JTextPane();
        jLabelMessageType = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonNickname = new javax.swing.JButton();
        jButtonClearSelection = new javax.swing.JButton();
        jLabelYou = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonDisconnect.setText("Disconnect");
        jButtonDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisconnectActionPerformed(evt);
            }
        });

        jButtonConnect.setText("Connect");
        jButtonConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConnectActionPerformed(evt);
            }
        });

        jButtonSend.setText("Send");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        jListOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListOnlineMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListOnline);

        jScrollPane2.setViewportView(jTextPaneChat);

        jLabelMessageType.setText("to Everyone:");

        jLabel1.setText("Haladinma Dafaka 3.0");

        jLabel2.setText("Chat Program");

        jButtonNickname.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jButtonNickname.setText("Nickname");
        jButtonNickname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNicknameActionPerformed(evt);
            }
        });

        jButtonClearSelection.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jButtonClearSelection.setText("Clear");
        jButtonClearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearSelectionActionPerformed(evt);
            }
        });

        jLabelYou.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabelYou.setText("You:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(jButtonConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDisconnect))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelMessageType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldSend))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonNickname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonClearSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(jLabelYou, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(13, 13, 13)
                                .addComponent(jScrollPane2)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonDisconnect)
                        .addComponent(jButtonConnect))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabelYou, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelMessageType, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                            .addComponent(jButtonClearSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonNickname, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed

        if (jLabelMessageType.equals("Change your 'Nickname' to: ")) {
            if (jTextFieldSend.getText().length() <= 8) {
                currentClient.send(Protocol.NICKNAME + jTextFieldSend.getText());
                jLabelYou.setText("You: " + jTextFieldSend.getText());
            } else {
                JOptionPane.showMessageDialog(null, "Nickname is too long.", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        } else if (jLabelMessageType.getText().contains("Everyone")) {
            currentClient.send(Protocol.SEND + Protocol.ALL + jTextFieldSend.getText());
        } else if (jLabelMessageType.getText().contains("People")) {
            String[] sendTo = jLabelMessageType.getText().split("-");
            StringBuffer s = new StringBuffer(sendTo[1]);
            StringBuffer AfterRemoval = s.deleteCharAt(sendTo[1].length() - 1);
            System.out.println(">>> " + Protocol.SEND + AfterRemoval.toString() + Protocol.HashTag + jTextFieldSend.getText());
            currentClient.send(Protocol.SEND + AfterRemoval.toString() + Protocol.HashTag + jTextFieldSend.getText());
        } else {
            currentClient.send(Protocol.SEND + jTextFieldSend.getText());
        }
        jTextFieldSend.setText("");
        jTextFieldSend.requestFocus();
        jLabelMessageType.setText("to Everyone:");
    }//GEN-LAST:event_jButtonSendActionPerformed

    private void jButtonConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConnectActionPerformed
        currentClient = new Client();
//if statement if that client with that name is connected.
        Object name = JOptionPane.showInputDialog("Nickname:");
        if (name.toString().length() <= 8) {
            int port = 9090;
            String ip = "localhost";
            try {

                currentClient.connect(ip, port); //connects to server
                currentClient.registerEchoListener(this); //registers itself
                currentClient.send(Protocol.CONNECT + name.toString());

                jLabelYou.setText("You: " + name);
                jButtonDisconnect.setEnabled(true);
                jButtonSend.setEnabled(true);
                jButtonNickname.setEnabled(true);
                jTextFieldSend.setEnabled(true);
                jButtonConnect.setEnabled(false);
                jTextPaneChat.setEnabled(true);
                jListOnline.setEnabled(true);
                jTextPaneChat.setEditable(false);
                jLabelMessageType.setText("to Everyone:");
            } catch (UnknownHostException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nickname is too long.", "ERROR", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButtonConnectActionPerformed

    private void jButtonDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisconnectActionPerformed
        try {
            currentClient.stopClient();
            currentClient.unRegisterEchoListener(this);
            jLabelYou.setText("You: ");
            jButtonDisconnect.setEnabled(false);
            jButtonSend.setEnabled(false);
            jButtonNickname.setEnabled(false);
            jTextFieldSend.setEnabled(false);
            jButtonConnect.setEnabled(true);
            jTextPaneChat.setEnabled(false);
            jListOnline.setEnabled(false);
            jTextPaneChat.setEditable(true);
            onlineClients.clear();
            //jTextPaneChat.setText("");
        } catch (IOException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDisconnectActionPerformed

    private void jListOnlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListOnlineMouseClicked
        if (jLabelMessageType.getText().contains("Everyone")) {
            jLabelMessageType.setText("to People-" + jListOnline.getSelectedValue().toString() + ":");
        } else {
            if (!jLabelMessageType.getText().contains(jListOnline.getSelectedValue().toString())) {
                jLabelMessageType.setText(jLabelMessageType.getText().replace(":", "," + jListOnline.getSelectedValue().toString() + ":"));
            }
        }
        jListOnline.clearSelection();
    }//GEN-LAST:event_jListOnlineMouseClicked

    private void jButtonNicknameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNicknameActionPerformed
        jLabelMessageType.setText("Change your 'Nickname' to: ");
        jButtonNickname.setEnabled(false);
    }//GEN-LAST:event_jButtonNicknameActionPerformed

    private void jButtonClearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearSelectionActionPerformed
        jLabelMessageType.setText("to Everyone:");
    }//GEN-LAST:event_jButtonClearSelectionActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClearSelection;
    private javax.swing.JButton jButtonConnect;
    private javax.swing.JButton jButtonDisconnect;
    private javax.swing.JButton jButtonNickname;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelMessageType;
    private javax.swing.JLabel jLabelYou;
    private javax.swing.JList jListOnline;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldSend;
    private javax.swing.JTextPane jTextPaneChat;
    // End of variables declaration//GEN-END:variables
    private DefaultListModel onlineClients = new DefaultListModel();

    @Override
    public void messageArrived(String data) {
        System.out.println("messageArrived(String data) (ClientGUI): " + data);
        String toBeDisplayed = "";
        if (data.contains(Protocol.ONLINE)) {
            onlineClients.clear();
            String[] result = data.split("#");
            List<String> items = Arrays.asList(result[1].split("\\s*,\\s*"));
            for (int i = 0; i < items.size(); i++) {
                onlineClients.addElement(items.get(i));
            }
            jListOnline.setModel(onlineClients);
        }
        for (int i = 0; i < currentClient.globalMessage.size(); i++) {
            toBeDisplayed += currentClient.globalMessage.get(i) + "\n";
        }
        jTextPaneChat.setText(toBeDisplayed);
    }
}
