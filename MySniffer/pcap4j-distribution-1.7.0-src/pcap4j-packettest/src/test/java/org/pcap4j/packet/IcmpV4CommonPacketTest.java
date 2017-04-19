package org.pcap4j.packet;

import static org.junit.Assert.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcap4j.packet.IcmpV4CommonPacket.IcmpV4CommonHeader;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IcmpV4Code;
import org.pcap4j.packet.namednumber.IcmpV4Type;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("javadoc")
public class IcmpV4CommonPacketTest extends AbstractPacketTest {

  private static final Logger logger
    = LoggerFactory.getLogger(IcmpV4CommonPacketTest.class);

  private final IcmpV4CommonPacket packet;
  private final IcmpV4Type type;
  private final IcmpV4Code code;
  private final short checksum;

  public IcmpV4CommonPacketTest() {
    UnknownPacket.Builder unknownb = new UnknownPacket.Builder();
    unknownb.rawData(new byte[] { (byte)0, (byte)1, (byte)2, (byte)3 });

    IcmpV4EchoPacket.Builder echob = new IcmpV4EchoPacket.Builder();
    echob.identifier((short)100)
         .sequenceNumber((short)10)
         .payloadBuilder(unknownb);

    this.type = IcmpV4Type.ECHO;
    this.code = IcmpV4Code.NO_CODE;
    this.checksum = (short)0x1234;

    IcmpV4CommonPacket.Builder b = new IcmpV4CommonPacket.Builder();
    b.type(type)
     .code(code)
     .checksum(checksum)
     .correctChecksumAtBuild(false)
     .payloadBuilder(echob);
    this.packet = b.build();
  }

  @Override
  protected Packet getPacket() {
    return packet;
  }

  @Override
  protected Packet getWholePacket() throws UnknownHostException {
    IpV4Packet.Builder ipv4b = new IpV4Packet.Builder();
    ipv4b.version(IpVersion.IPV4)
         .tos(IpV4Rfc1349Tos.newInstance((byte)0))
         .identification((short)100)
         .ttl((byte)100)
         .protocol(IpNumber.ICMPV4)
         .srcAddr(
            (Inet4Address)InetAddress.getByAddress(
              new byte[] { (byte)192, (byte)0, (byte)2, (byte)1 }
            )
          )
        .dstAddr(
           (Inet4Address)InetAddress.getByAddress(
             new byte[] { (byte)192, (byte)0, (byte)2, (byte)2 }
           )
         )
        .payloadBuilder(packet.getBuilder().correctChecksumAtBuild(true))
        .correctChecksumAtBuild(true)
        .correctLengthAtBuild(true);

    EthernetPacket.Builder eb = new EthernetPacket.Builder();
    eb.dstAddr(MacAddress.getByName("fe:00:00:00:00:02"))
      .srcAddr(MacAddress.getByName("fe:00:00:00:00:01"))
      .type(EtherType.IPV4)
      .payloadBuilder(ipv4b)
      .paddingAtBuild(true);
    return eb.build();
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    logger.info(
      "########## " + IcmpV4CommonPacketTest.class.getSimpleName() + " START ##########"
    );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testNewPacket() {
    try {
      IcmpV4CommonPacket p
        = IcmpV4CommonPacket.newPacket(packet.getRawData(), 0, packet.getRawData().length);
      assertEquals(packet, p);
    } catch (IllegalRawDataException e) {
      throw new AssertionError(e);
    }
  }

  @Test
  public void testNewPacketRandom() {
      RandomPacketTester.testClass(IcmpV4CommonPacket.class, packet);
  }

  @Test
  public void testGetHeader() {
    IcmpV4CommonHeader h = packet.getHeader();
    assertEquals(type, h.getType());
    assertEquals(code, h.getCode());
    assertEquals(checksum, h.getChecksum());
  }

  @Test
  public void testHasValidChecksum() {
    assertFalse(packet.hasValidChecksum(false));
    assertFalse(packet.hasValidChecksum(true));


    IcmpV4CommonPacket.Builder b = packet.getBuilder();
    IcmpV4CommonPacket p;

    b.checksum((short)0).correctChecksumAtBuild(false);
    p = b.build();
    assertFalse(p.hasValidChecksum(false));
    assertTrue(p.hasValidChecksum(true));

    b.correctChecksumAtBuild(true);
    p = b.build();
    assertTrue(p.hasValidChecksum(false));
    assertTrue(p.hasValidChecksum(true));
  }

}
