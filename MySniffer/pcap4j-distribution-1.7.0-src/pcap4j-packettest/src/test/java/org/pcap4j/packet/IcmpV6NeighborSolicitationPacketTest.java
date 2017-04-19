package org.pcap4j.packet;

import static org.junit.Assert.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcap4j.packet.IcmpV6CommonPacket.IpV6NeighborDiscoveryOption;
import org.pcap4j.packet.IcmpV6NeighborSolicitationPacket.IcmpV6NeighborSolicitationHeader;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IcmpV6Code;
import org.pcap4j.packet.namednumber.IcmpV6Type;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("javadoc")
public class IcmpV6NeighborSolicitationPacketTest extends AbstractPacketTest {

  private static final Logger logger
    = LoggerFactory.getLogger(IcmpV6NeighborSolicitationPacketTest.class);

  private final IcmpV6NeighborSolicitationPacket packet;
  private final int reserved;
  private final Inet6Address targetAddress;
  private final List<IpV6NeighborDiscoveryOption> options
    = new ArrayList<IpV6NeighborDiscoveryOption>();

  public IcmpV6NeighborSolicitationPacketTest() throws UnknownHostException {
    this.reserved = 123454321;
    this.targetAddress = (Inet6Address)InetAddress.getByName("2001:db8::aaaa:bbbb:0:0");

    IpV6NeighborDiscoverySourceLinkLayerAddressOption.Builder opt
      = new IpV6NeighborDiscoverySourceLinkLayerAddressOption.Builder();
    opt.linkLayerAddress(
          new byte[] {
            (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03
          }
        )
       .correctLengthAtBuild(true);
    this.options.add(opt.build());

    IcmpV6NeighborSolicitationPacket.Builder b = new IcmpV6NeighborSolicitationPacket.Builder();
    b.reserved(reserved)
     .targetAddress(targetAddress)
     .options(options);
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
    icmpV6b.type(IcmpV6Type.NEIGHBOR_SOLICITATION)
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
      "########## " + IcmpV6NeighborSolicitationPacketTest.class.getSimpleName() + " START ##########"
    );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testNewPacket() {
    try {
      IcmpV6NeighborSolicitationPacket p
        = IcmpV6NeighborSolicitationPacket
            .newPacket(packet.getRawData(), 0, packet.getRawData().length);
      assertEquals(packet, p);
    } catch (IllegalRawDataException e) {
      throw new AssertionError(e);
    }
  }

  @Test
  public void testGetHeader() {
    IcmpV6NeighborSolicitationHeader h = packet.getHeader();
    assertEquals(targetAddress, h.getTargetAddress());
    assertEquals(reserved, h.getReserved());
    Iterator<IpV6NeighborDiscoveryOption> iter = h.getOptions().iterator();
    for (IpV6NeighborDiscoveryOption expected: options) {
      IpV6NeighborDiscoveryOption actual = iter.next();
      assertEquals(expected, actual);
    }
  }

}
