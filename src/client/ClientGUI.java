package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import shared.Protocol;

public class ClientGUI extends javax.swing.JFrame implements EchoListener {

    
    
    Client currentClient;

    public ClientGUI() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonDisconnect = new javax.swing.JButton();
        jButtonConnect = new javax.swing.JButton();
        jTextFieldSend = new javax.swing.JTextField();
        jButtonSend = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneChat = new javax.swing.JTextPane();

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

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jScrollPane2.setViewportView(jTextPaneChat);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSend))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonConnect)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDisconnect))
                    .addComponent(jTextFieldSend, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(125, 125, 125)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonSend)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonDisconnect)
                            .addComponent(jButtonConnect))
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(60, 60, 60)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(49, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed
        try {
            String toBeDisplayed = "";
            currentClient.send(Protocol.MESSAGE + jTextFieldSend.getText());
            jTextFieldSend.setText("");
            Thread.sleep(100);
            for (int i = 0; i < currentClient.globalMessage.size(); i++) {
                toBeDisplayed += currentClient.globalMessage.get(i) + "\n";
            }
            //toBeDisplayed += currentClient.get
            jTextPaneChat.setText(toBeDisplayed);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTextFieldSend.requestFocus();
    }//GEN-LAST:event_jButtonSendActionPerformed

    private void jButtonConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConnectActionPerformed
                currentClient = new Client();
        
        if (!currentClient.isClientConnected(currentClient)) {
             Object name = JOptionPane.showInputDialog("Enter bankacc:");

            int port = 9090;
            String ip = "localhost";
            try {

                currentClient.connect(ip, port); //connects to server
                currentClient.registerEchoListener(this); //registers itself
                currentClient.send("/n " + name.toString());
            } catch (UnknownHostException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "U R ALREADY IN, NIGGA? YOLO MUCH?", "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonConnectActionPerformed

    private void jButtonDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisconnectActionPerformed
        try { 
            currentClient.stopClient();
            currentClient.unRegisterEchoListener(this);

        } catch (IOException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDisconnectActionPerformed

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
    private javax.swing.JButton jButtonConnect;
    private javax.swing.JButton jButtonDisconnect;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldSend;
    private javax.swing.JTextPane jTextPaneChat;
    // End of variables declaration//GEN-END:variables

    @Override
    public void messageArrived(String data) {
    String toBeDisplayed = "";
        for (int i = 0; i < currentClient.globalMessage.size(); i++) {
                toBeDisplayed += currentClient.globalMessage.get(i) + "\n";
            }
            jTextPaneChat.setText(toBeDisplayed);    
    }
}
