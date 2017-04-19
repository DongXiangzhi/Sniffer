package org.pcap4j.packet;

import static org.junit.Assert.*;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcap4j.packet.UdpPacket.UdpHeader;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.packet.namednumber.UdpPort;
import org.pcap4j.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("javadoc")
public class UdpPacketTest extends AbstractPacketTest {

  private static final Logger logger
    = LoggerFactory.getLogger(UdpPacketTest.class);

  private final UdpPort srcPort;
  private final UdpPort dstPort;
  private final short length;
  private final short checksum;
  private final Inet6Address srcAddr;
  private final Inet6Address dstAddr;
  private final UdpPacket packet;

  public UdpPacketTest() throws Exception {
    this.srcPort = UdpPort.SNMP;
    this.dstPort = UdpPort.getInstance((short)0);
    this.length = (short)12;
    this.checksum = (short)0xABCD;
    try {
      this.srcAddr
        = (Inet6Address)InetAddress.getByName("2001:db8::3:2:1");
      this.dstAddr
        = (Inet6Address)InetAddress.getByName("2001:db8::3:2:2");
    } catch (UnknownHostException e) {
      throw new AssertionError();
    }

    UnknownPacket.Builder unknownb = new UnknownPacket.Builder();
    unknownb.rawData(new byte[] { (byte)0, (byte)1, (byte)2, (byte)3 });

    UdpPacket.Builder b = new UdpPacket.Builder();
    b.dstPort(dstPort)
     .srcPort(srcPort)
     .length(length)
     .checksum(checksum)
     .correctChecksumAtBuild(false)
     .correctLengthAtBuild(false)
     .payloadBuilder(unknownb);

    this.packet = b.build();
  }

  @Override
  protected Packet getPacket() {
    return packet;
  }

  @Override
  protected Packet getWholePacket() {
    IpV6Packet.Builder IpV6b = new IpV6Packet.Builder();
    IpV6b.version(IpVersion.IPV6)
         .trafficClass(IpV6SimpleTrafficClass.newInstance((byte)0x12))
         .flowLabel(IpV6SimpleFlowLabel.newInstance(0x12345))
         .nextHeader(IpNumber.UDP)
         .hopLimit((byte)100)
         .srcAddr(srcAddr)
         .dstAddr(dstAddr)
         .payloadBuilder(
            packet.getBuilder()
              .correctChecksumAtBuild(true)
              .correctLengthAtBuild(true)
          )
         .correctLengthAtBuild(true);

    EthernetPacket.Builder eb = new EthernetPacket.Builder();
    eb.dstAddr(MacAddress.getByName("fe:00:00:00:00:02"))
      .srcAddr(MacAddress.getByName("fe:00:00:00:00:01"))
      .type(EtherType.IPV6)
      .payloadBuilder(IpV6b)
      .paddingAtBuild(true);

    eb.get(UdpPacket.Builder.class)
      .dstAddr(dstAddr)
      .srcAddr(srcAddr);
    return eb.build();
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    logger.info(
      "########## " + UdpPacketTest.class.getSimpleName() + " START ##########"
    );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testNewPacket() {
    try {
      UdpPacket p = UdpPacket.newPacket(packet.getRawData(), 0, packet.getRawData().length);
      assertEquals(packet, p);
    } catch (IllegalRawDataException e) {
      throw new AssertionError(e);
    }
  }

  @Test
  public void testNewPacketRandom() {
      RandomPacketTester.testClass(UdpPacket.class, packet);
  }

  @Test
  public void testGetHeader() {
    UdpHeader h = packet.getHeader();
    assertEquals(srcPort, h.getSrcPort());
    assertEquals(dstPort, h.getDstPort());
    assertEquals(length, h.getLength());
    assertEquals(checksum, h.getChecksum());

    UdpPacket.Builder b = packet.getBuilder();
    UdpPacket p;

    b.length((short)0);
    p = b.build();
    assertEquals((short)0, (short)p.getHeader().getLengthAsInt());

    b.length((short)-1);
    p = b.build();
    assertEquals((short)-1, (short)p.getHeader().getLengthAsInt());

    b.length((short)32767);
    p = b.build();
    assertEquals((short)32767, (short)p.getHeader().getLengthAsInt());

    b.length((short)-32768);
    p = b.build();
    assertEquals((short)-32768, (short)p.getHeader().getLengthAsInt());
  }

  @Test
  public void testHasValidChecksum() {
    UdpPacket.Builder b = packet.getBuilder();
    b.srcAddr(srcAddr).dstAddr(dstAddr);
    UdpPacket p = b.correctChecksumAtBuild(false).build();

    assertFalse(packet.hasValidChecksum(srcAddr, dstAddr, false));
    assertFalse(packet.hasValidChecksum(srcAddr, dstAddr, true));

    b.checksum((short)0).correctChecksumAtBuild(false);
    p = b.build();
    assertFalse(p.hasValidChecksum(srcAddr, dstAddr, false));
    assertTrue(p.hasValidChecksum(srcAddr, dstAddr, true));

    b.correctChecksumAtBuild(true);
    p = b.build();
    assertTrue(p.hasValidChecksum(srcAddr, dstAddr, false));
    assertTrue(p.hasValidChecksum(srcAddr, dstAddr, true));
  }

}
