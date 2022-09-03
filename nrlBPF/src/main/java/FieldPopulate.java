import java.util.ArrayList;

public class FieldPopulate {

	public static Field protocol_one() {
		Field field = new Field("Protocol","Ethertype.2048", 8, 184);
		return field;
	}
	
	public static Field protocol_two() {
		Field field = new Field("Protocol","Ethertype.34525", 8, 160);
		return field;
	}
	
	public static Field ethertype() {
		Field field = new Field("Ethertype","", 16, 96);
		return field;
	}
	
	public static Field portSrc() {
		Field field = new Field("PortSRC","Ethertype.34525", 16, 416);
		return field;
	}
	
	public static Field portDst() {
		Field field = new Field("PortDest","Ethertype.34525", 16, 432);
		return field;
	}
	
	public static Field portSrcx() {
		Field field = new Field("PortSRC","Ethertype.2048", 16, 256);
		return field;
	}
	public static Field portDstcx() {
		Field field = new Field("PortDest","Ethertype.2048", 16, 272);
		return field;
	}


	public static Field ipsrc_1() {
		Field field = new Field("IPSrc","Ethertype.34525", 32, 224);
		return field;
	}
	
	public static Field ipsrc_2() {
		Field field = new Field("IPSrc","Ethertype.2048", 32, 208);
		return field;
	}
	
	public static Field ipdst_1() {
		Field field = new Field("IPDest","Ethertype.34525", 32, 304);
		return field;
	}
	
	public static Field ipdst_2() {
		Field field = new Field("IPDest","Ethertype.2048", 32, 240);
		return field;
	}
	
	public static ArrayList<Field> returnList(){
		
		ArrayList<Field> list = new ArrayList<Field>();
		list.add(FieldPopulate.protocol_one());
		list.add(FieldPopulate.protocol_two());
		list.add(FieldPopulate.ethertype());
		
		list.add(FieldPopulate.portDst());
		list.add(FieldPopulate.portDstcx());
		list.add(FieldPopulate.portSrc());
		list.add(FieldPopulate.portSrcx());
		
		list.add(FieldPopulate.ipdst_1());
		list.add(FieldPopulate.ipdst_2());
		list.add(FieldPopulate.ipsrc_1());
		list.add(FieldPopulate.ipsrc_2());
		
		return list;
	}
}
