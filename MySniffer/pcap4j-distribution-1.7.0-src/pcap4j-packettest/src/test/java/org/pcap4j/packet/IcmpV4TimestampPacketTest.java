package org.pcap4j.packet;

import static org.junit.Assert.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcap4j.packet.IcmpV4TimestampPacket.IcmpV4TimestampHeader;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IcmpV4Code;
import org.pcap4j.packet.namednumber.IcmpV4Type;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("javadoc")
public class IcmpV4TimestampPacketTest extends AbstractPacketTest {

  private static final Logger logger
    = LoggerFactory.getLogger(IcmpV4TimestampPacketTest.class);

  private final IcmpV4TimestampPacket packet;
  private final short identifier;
  private final short sequenceNumber;
  private final int originateTimestamp;
  private final int receiveTimestamp;
  private final int transmitTimestamp;

  public IcmpV4TimestampPacketTest() {
    this.identifier = (short)1234;
    this.sequenceNumber = (short)4321;
    this.originateTimestamp = 10;
    this.receiveTimestamp = 200;
    this.transmitTimestamp = 3000;

    IcmpV4TimestampPacket.Builder b = new IcmpV4TimestampPacket.Builder();
    b.identifier(identifier)
     .sequenceNumber(sequenceNumber)
     .originateTimestamp(originateTimestamp)
     .receiveTimestamp(receiveTimestamp)
     .transmitTimestamp(transmitTimestamp);
    this.packet = b.build();
  }

  @Override
  protected Packet getPacket() {
    return packet;
  }

  @Override
  protected Packet getWholePacket() throws UnknownHostException {
    IcmpV4CommonPacket.Builder icmpV4b = new IcmpV4CommonPacket.Builder();
    icmpV4b.type(IcmpV4Type.TIMESTAMP)
           .code(IcmpV4Code.NO_CODE)
           .payloadBuilder(new SimpleBuilder(packet))
           .correctChecksumAtBuild(true);

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
        .payloadBuilder(icmpV4b)
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
      "########## " + IcmpV4TimestampPacketTest.class.getSimpleName() + " START ##########"
    );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testNewPacket() {
    try {
      IcmpV4TimestampPacket p
        = IcmpV4TimestampPacket.newPacket(packet.getRawData(), 0, packet.getRawData().length);
      assertEquals(packet, p);
    } catch (IllegalRawDataException e) {
      throw new AssertionError(e);
    }
  }

  @Test
  public void testGetHeader() {
    IcmpV4TimestampHeader h = packet.getHeader();
    assertEquals(identifier, h.getIdentifier());
    assertEquals(sequenceNumber, h.getSequenceNumber());
    assertEquals(originateTimestamp, h.getOriginateTimestamp());
    assertEquals(receiveTimestamp, h.getReceiveTimestamp());
    assertEquals(transmitTimestamp, h.getTransmitTimestamp());

    IcmpV4TimestampPacket.Builder b = packet.getBuilder();
    IcmpV4TimestampPacket p;

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
