
import java.awt.*;
import ch.ebu.reliabletp.packets.*;

public class frmRTCPMonitor extends Frame implements RTP_actionListener, RTCP_actionListener {
    private static String IP;
    private static int Port;

    // Used for addNotify check.
    boolean fComponentsAdjusted = false;

    //{{DECLARE_CONTROLS
    java.awt.TextField txtAddress;
    java.awt.Label label1;
    java.awt.Button cmdConnect;
    java.awt.Button cmdShowRTCP;
    java.awt.Button cmdShowRTP;
    java.awt.Label label3;
    java.awt.Button cmdExit;

    public frmRTCPMonitor() {
        // This code is automatically generated by Visual Cafe when you add
        // components to the visual environment. It instantiates and initializes
        // the components. To modify the code, only use code syntax that matches
        // what Visual Cafe can generate, or Visual Cafe may be unable to back
        // parse your Java file into its visual environment.
        //{{INIT_CONTROLS
        setLayout(null);
        setVisible(false);
        setSize(insets().left + insets().right + 266, insets().top + insets().bottom + 169);
        txtAddress = new java.awt.TextField();
        txtAddress.setText("234.5.6.7/8000");
        txtAddress.setBounds(insets().left + 12, insets().top + 36, 156, 24);
        txtAddress.setBackground(new Color(16777215));
        add(txtAddress);
        label1 = new java.awt.Label("Multicast Group Address/port");
        label1.setBounds(insets().left + 12, insets().top + 12, 216, 24);
        add(label1);
        cmdConnect = new java.awt.Button();
        cmdConnect.setLabel("Connect");
        cmdConnect.setBounds(insets().left + 168, insets().top + 36, 84, 24);
        cmdConnect.setBackground(new Color(12632256));
        add(cmdConnect);
        cmdShowRTCP = new java.awt.Button();
        cmdShowRTCP.setLabel("RTCP");
        cmdShowRTCP.setBounds(insets().left + 12, insets().top + 132, 72, 24);
        cmdShowRTCP.setBackground(new Color(12632256));
        add(cmdShowRTCP);
        cmdShowRTP = new java.awt.Button();
        cmdShowRTP.setLabel("RTP");
        cmdShowRTP.setBounds(insets().left + 12, insets().top + 96, 72, 24);
        cmdShowRTP.setBackground(new Color(12632256));
        add(cmdShowRTP);
        label3 = new java.awt.Label("Show the following packets");
        label3.setBounds(insets().left + 12, insets().top + 72, 165, 23);
        add(label3);
        cmdExit = new java.awt.Button();
        cmdExit.setLabel("Exit");
        cmdExit.setBounds(insets().left + 180, insets().top + 132, 72, 24);
        cmdExit.setBackground(new Color(12632256));
        add(cmdExit);
        setTitle("RTP Monitor");
        //}}

        //{{INIT_MENUS
        //}}

        //{{REGISTER_LISTENERS
        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);
        SymAction lSymAction = new SymAction();
        cmdConnect.addActionListener(lSymAction);
        cmdExit.addActionListener(lSymAction);
        SymMouse aSymMouse = new SymMouse();
        cmdExit.addMouseListener(aSymMouse);
        //}}

    }

    public frmRTCPMonitor(String title) {
        this();
        setTitle(title);

    }

    public synchronized void show() {
        move(50, 50);
        super.show();
    }

    public void addNotify() {
        // Record the size of the window prior to calling parents addNotify.
        Dimension d = getSize();

        super.addNotify();

        if (fComponentsAdjusted)
            return;

        // Adjust components according to the insets
        setSize(insets().left + insets().right + d.width, insets().top + insets().bottom + d.height);
        Component components[] = getComponents();
        for (int i = 0; i < components.length; i++) {
            Point p = components[i].getLocation();
            p.translate(insets().left, insets().top);
            components[i].setLocation(p);
        }
        fComponentsAdjusted = true;
    }

    //}}

    //{{DECLARE_MENUS
    //}}

    class SymWindow extends java.awt.event.WindowAdapter {
        public void windowClosing(java.awt.event.WindowEvent event) {
            Object object = event.getSource();
            if (object == frmRTCPMonitor.this)
                Frame1_WindowClosing(event);
        }
    }

    void Frame1_WindowClosing(java.awt.event.WindowEvent event) {
        hide();         // hide the Frame
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == cmdConnect)
                cmdConnect_Action(event);
            else if (object == cmdExit)
                cmdExit_Action(event);
        }
    }

    void cmdConnect_Action(java.awt.event.ActionEvent event) {
        // Toggle the connect disconnect button
        if (cmdConnect.getLabel().equals("Connect")) {
            txtAddress.enable(false);
            cmdConnect.setLabel("Disconnect");
        } else {
            txtAddress.enable(true);
            cmdConnect.setLabel("Connect");
        }

        // Validate the internet address in the field and extract IP and Port from IP/Port
        String IPPort = this.txtAddress.getText();

        int slashLocation = 0;

        if ((slashLocation = IPPort.indexOf("/")) == -1) {
            System.err.println(" Slash character not found in the field: " + IPPort);
            return;
        }

        IP = IPPort.substring(0, slashLocation);
        System.out.println("IP:" + IP);

        try {
            Port = Integer.parseInt(IPPort.substring(slashLocation + 1));
        } catch (java.lang.NumberFormatException e) {
            System.err.println("Invalid Port number (must be an integer) ");
            return;
        }

        System.out.println("Port:" + Port);

        if (Port > 36768) {
            System.err.println("Port number larger than 36768");
            return;
        }
        // Port is fine now, proceed with creating an RTPSession

    }

    void cmdExit_Action(java.awt.event.ActionEvent event) {
        // Disconnect from the Multicast group if connected

        // Exit the system
        // SYstem.
    }

    public void handleRTPEvent(RTPPacket rtppkt) {
        ;
    }

    public void handleRTCPEvent(RTCPReceiverReportPacket rrpkt) {
        ;
    }

    public void handleRTCPEvent(RTCPSenderReportPacket srpkt) {
        ;
    }

    public void handleRTCPEvent(RTCPSDESPacket sdespkt) {
        ;
    }

    public void handleRTCPEvent(RTCPBYEPacket byepkt) {
        ;
    }


    class SymMouse extends java.awt.event.MouseAdapter {
        public void mouseClicked(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object == cmdExit)
                cmdExit_MouseClick(event);
        }
    }

    void cmdExit_MouseClick(java.awt.event.MouseEvent event) {
        // Disconnect session
        CMain.DestroyFrame();
    }

    void StartSession() {
        ;
    }

}