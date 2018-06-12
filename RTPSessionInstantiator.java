import ch.ebu.reliabletp.LateInitializationClient;
import ch.ebu.reliabletp.LateInitializationServer;
import ch.ebu.reliabletp.PacketUtils;
import ch.ebu.reliabletp.packets.*;
import ch.ebu.reliabletp.Session;
import java.util.Enumeration;

public class RTPSessionInstantiator extends java.lang.Object
        implements ch.ebu.reliabletp.packets.RTP_actionListener,
        ch.ebu.reliabletp.packets.RTCP_actionListener {
    Session rtpSession;

    public static void main(String args[]) {

        RTPSessionInstantiator r = new RTPSessionInstantiator(args);
    }


    RTPSessionInstantiator(String args[]) {

        System.out.println(args.length + " arguments");

        rtpSession = new Session("234.5.6.7",    //  MulticastGroupIPAddress
                8000,           //  MulticastGroupPort
                8001,           //  RTCPGroupPort
                8051,           //  RTPSendFromPort
                8052,           //  RTCPSendFromPort
                10000           //  bandwidth
        );

        rtpSession.setPayloadType(5);
        rtpSession.setCName("RTPUser");
        rtpSession.setEMail("BhaadeKeTatou@rainbow.com");

        rtpSession.DebugOutput = true;

        if (args[0].equals("LS")) {
            Session.InitializationData.addElement("One");
            Session.InitializationData.addElement("Two");
            Session.InitializationData.addElement("Three");
            Session.InitializationData.addElement("Four");
            Session.InitializationData.addElement("Five");
            Session.InitializationData.addElement("Six");

            LateInitializationServer lateServer = new LateInitializationServer();
            lateServer.StartServerDeamon(9000);
        }

        if (args[0].equals("LC")) {
            LateInitializationClient lateClient = new LateInitializationClient();
            lateClient.StartClient(args[1], 9000);

            // wait for the client thread to finish receiving data before
            // further execution.

            while (true) {
                Enumeration enumx = Session.InitializationData.elements();
                while (enumx.hasMoreElements()) {
                    Session.outprintln((String) enumx.nextElement());
                }
            }
        }

        // Declare myslef as the only object who will receive
        // RTP and RTCP packet reception events.
        rtpSession.addRTP_actionListener(this);
        rtpSession.addRTCP_actionListener(this);

        byte ss[] = PacketUtils.LongToBytes(Session.SSRC, 8);

        System.out.println("SSRC = 0x" + Long.toHexString(Session.SSRC));

        //rtpSession.StartRTPReceiverThread(  );

        if (args[0].equals("R")) {
            rtpSession.StartRTCPSenderThread();
            rtpSession.StartRTCPReceiverThread();
            rtpSession.StartRTPReceiverThread();

        }

        if (args[0].equals("S")) {
            // Tesing out by sending only RTP packets
            rtpSession.StartRTCPSenderThread();
            int i = 0;
            while (i < 1000) {
                rtpSession.m_RTPHandler.sendTestPacket("Test Packet");
                i++;
            }

            rtpSession.StopRTCPSenderThread();
        }

        if (args[0].equals("A")) {
            rtpSession.StartRTCPSenderThread();
            rtpSession.StartRTCPReceiverThread();
            rtpSession.StartRTPReceiverThread();
            int i = 0;

            while (i < 1000) {
                Session.outprintln("\n***********************************************************");
                rtpSession.m_RTPHandler.sendTestPacket("Test Packet");
                i++;
            }

            rtpSession.StopRTCPSenderThread();

        }

        if (args[0].equals("AB")) {
            int NoOfPkts = Integer.parseInt(args[1]);

            rtpSession.StartRTCPSenderThread();
            rtpSession.StartRTCPReceiverThread();
            rtpSession.StartRTPReceiverThread();
            int i = 0;

            while (i < NoOfPkts) {
                Session.outprintln("\n***********************************************************");
                rtpSession.m_RTPHandler.sendTestPacket("Test packet");
                i++;
            }

            System.out.println("Sending a bye");
            rtpSession.IsByeRequested = true;
            rtpSession.StopRTCPSenderThread();


        }

        if (args[0].equals("C")) {
            //Session.
        }


    }

    public void handleRTPEvent(RTPPacket rtppkt) {
        System.out.println("**** ActionListener RTP: *****\n"
                + "CSRC: " + rtppkt.CSRCCount + "\n"
                + "Seq No: " + rtppkt.SequenceNumber + "\n"
                + "Timestmp: " + rtppkt.TimeStamp + "\n"
                + "SSRC: " + Long.toHexString(rtppkt.SSRC) + "\n"
                + "Data Length: " + rtppkt.data.length + "\n"
                + "Data: {" + new String(rtppkt.data) + "}" + "\n"
                + "\n*******************************\n");
    }

    public void handleRTCPEvent(RTCPReceiverReportPacket RRpkt) {
        System.out.println("\n**** ActionListener RTCP RR Packet: *****\n"
                + "SSRC = " + Long.toHexString(RRpkt.SenderSSRC) + "\n"
                + "ReportBlock = " + RRpkt.containsReportBlock + "\n"
                + "FractionLost = " + RRpkt.ReportBlock.FractionLost + "\n"
                + "CumPktsLost = " + RRpkt.ReportBlock.CumulativeNumberOfPacketsLost + "\n"
                + "ExtHighSqRcvd = " + RRpkt.ReportBlock.ExtendedHighestSequenceNumberReceived + "\n"
                + "IntJitter = " + RRpkt.ReportBlock.InterarrivalJitter + "\n"
                + "LastSR = " + RRpkt.ReportBlock.LastSR + "\n"
                + "Delay_LastSR = " + RRpkt.ReportBlock.Delay_LastSR + "\n"
        );

    }

    public void handleRTCPEvent(RTCPSenderReportPacket SRpkt) {
        System.out.println("\n**** ActionListener RTCP SR Packet: *****\n"
                + "SSRC = " + Long.toHexString(SRpkt.SenderSSRC) + "\n"
                + "SenderOctetCount:" + SRpkt.SenderInfo.SenderOctetCount + "\n"
                + " SenderPacketCount:" + SRpkt.SenderInfo.SenderPacketCount + "\n"
                + "RTPTimeStamp" + SRpkt.SenderInfo.RTPTimeStamp + "\n"
                + "NTPTimeStampLeastSignificant" + SRpkt.SenderInfo.NTPTimeStampLeastSignificant + "\n"
                + "NTPTimeStampMostSignificant" + SRpkt.SenderInfo.NTPTimeStampMostSignificant + "\n"
                + "ReportBlock = " + SRpkt.containsReportBlock + "\n");
        if (SRpkt.containsReportBlock)
            System.out.println(
                    "FractionLost = " + SRpkt.ReportBlock.FractionLost + "\n"
                            + "CumPktsLost = " + SRpkt.ReportBlock.CumulativeNumberOfPacketsLost + "\n"
                            + "ExtHighSqRcvd = " + SRpkt.ReportBlock.ExtendedHighestSequenceNumberReceived + "\n"
                            + "IntJitter = " + SRpkt.ReportBlock.InterarrivalJitter + "\n"
                            + "LastSR = " + SRpkt.ReportBlock.LastSR + "\n"
                            + "Delay_LastSR = " + SRpkt.ReportBlock.Delay_LastSR + "\n"
            );
    }

    public void handleRTCPEvent(RTCPSDESPacket sdespkt) {
        System.out.println("\n**** ActionListener RTCP SDES: *****\n"
                + "SDES Type: " + sdespkt.SDESItem.Type + "\n"
                + "SDES Value: " + sdespkt.SDESItem.Value + "\n"
                + "**************************************\n");
    }

    public void handleRTCPEvent(RTCPBYEPacket byepkt) {
        System.out.println("\n**** ActionListener RTCP BYE: *****\n"
                + "BYE SSRC: " + byepkt.SSRC + "\n"
                + "REason For Leaving " + byepkt.ReasonForLeaving + "\n"
                + "**************************************\n");
        ;
    }
}