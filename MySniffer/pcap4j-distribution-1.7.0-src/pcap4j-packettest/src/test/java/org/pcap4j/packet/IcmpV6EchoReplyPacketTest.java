package org.pcap4j.packet;

import static org.junit.Assert.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcap4j.packet.IcmpV6EchoReplyPacket.IcmpV6EchoReplyHeader;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IcmpV6Code;
import org.pcap4j.packet.namednumber.IcmpV6Type;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("javadoc")
public class IcmpV6EchoReplyPacketTest extends AbstractPacketTest {

  private static final Logger logger
    = LoggerFactory.getLogger(IcmpV6EchoReplyPacketTest.class);

  private final IcmpV6EchoReplyPacket packet;
  private final short identifier;
  private final short sequenceNumber;

  public IcmpV6EchoReplyPacketTest() {
    this.identifier = (short)1234;
    this.sequenceNumber = (short)4321;

    UnknownPacket.Builder unknownb = new UnknownPacket.Builder();
    unknownb.rawData(new byte[] { (byte)0, (byte)1, (byte)2, (byte)3 });

    IcmpV6EchoReplyPacket.Builder b = new IcmpV6EchoReplyPacket.Builder();
    b.identifier(identifier)
     .sequenceNumber(sequenceNumber)
     .payloadBuilder(unknownb);
    this.packet = b.build();
  }

  @Override
  protected Packet getPacket() {
    return packet;
  }

  @Override
  protected Packet getWholePacket() {
    Inet6Address srcAddr;
    Inet6Address dstAddr;
    try {
      srcAddr = (Inet6Address)InetAddress.getByName("2001:db8::3:2:1");
      dstAddr = (Inet6Address)InetAddress.getByName("2001:db8::3:2:2");
    } catch (UnknownHostException e) {
      throw new AssertionError();
    }
    IcmpV6CommonPacket.Builder icmpV6b = new IcmpV6CommonPacket.Builder();
    icmpV6b.type(IcmpV6Type.ECHO_REPLY)
           .code(IcmpV6Code.NO_CODE)
           .srcAddr(srcAddr)
           .dstAddr(dstAddr)
           .payloadBuilder(new SimpleBuilder(packet))
           .correctChecksumAtBuild(true);

    IpV6Packet.Builder ipv6b = new IpV6Packet.Builder();
    ipv6b.version(IpVersion.IPV6)
         .trafficClass(IpV6SimpleTrafficClass.newInstance((byte)0x12))
         .flowLabel(IpV6SimpleFlowLabel.newInstance(0x12345))
         .nextHeader(IpNumber.ICMPV6)
         .hopLimit((byte)100)
         .srcAddr(srcAddr)
         .dstAddr(dstAddr)
         .correctLengthAtBuild(true)
         .payloadBuilder(icmpV6b);

    EthernetPacket.Builder eb = new EthernetPacket.Builder();
    eb.dstAddr(MacAddress.getByName("fe:00:00:00:00:02"))
      .srcAddr(MacAddress.getByName("fe:00:00:00:00:01"))
      .type(EtherType.IPV6)
      .payloadBuilder(ipv6b)
      .paddingAtBuild(true);
    return eb.build();
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    logger.info(
      "########## " + IcmpV6EchoReplyPacketTest.class.getSimpleName() + " START ##########"
    );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testNewPacket() {
    try {
      IcmpV6EchoReplyPacket p
        = IcmpV6EchoReplyPacket
            .newPacket(packet.getRawData(), 0, packet.getRawData().length);
      assertEquals(packet, p);
    } catch (IllegalRawDataException e) {
      throw new AssertionError(e);
    }
  }

  @Test
  public void testGetHeader() {
    IcmpV6EchoReplyHeader h = packet.getHeader();
    assertEquals(identifier, h.getIdentifier());
    assertEquals(sequenceNumber, h.getSequenceNumber());

    IcmpV6EchoReplyPacket.Builder b = packet.getBuilder();
    IcmpV6EchoReplyPacket p;

    b.identifier((short)0);
    b.sequenceNumber((short)0);
    p = b.build();
    assertEquals((short)0, (short)p.getHeader().getIdentifierAsInt());
    assertEquals((short)0, (short)p.getHeader().getSequenceNumberAsInt());

    b.identifier((short)10000);
    b.sequenceNumber((short)10000);
    p = b.build();
    assertEquals((short)10000, (short)p.getHeader().getIdentifierAsInt());
    assertEquals((short)10000, (short)p.getHeader().getSequenceNumberAsInt());

    b.identifier((short)32767);
    b.sequenceNumber((short)32767);
    p = b.build();
    assertEquals((short)32767, (short)p.getHeader().getIdentifierAsInt());
    assertEquals((short)32767, (short)p.getHeader().getSequenceNumberAsInt());

    b.identifier((short)-1);
    b.sequenceNumber((short)-1);
    p = b.build();
    assertEquals((short)-1, (short)p.getHeader().getIdentifierAsInt());
    assertEquals((short)-1, (short)p.getHeader().getSequenceNumberAsInt());

    b.identifier((short)-32768);
    b.sequenceNumber((short)-32768);
    p = b.build();
    assertEquals((short)-32768, (short)p.getHeader().getIdentifierAsInt());
    assertEquals((short)-32768, (short)p.getHeader().getSequenceNumberAsInt());
  }

}
