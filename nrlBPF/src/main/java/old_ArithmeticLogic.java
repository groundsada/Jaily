
public class old_ArithmeticLogic {
	
	public static String loadEthernet() {
		return "ldh\t[12]\n";
	}
	
	public static String filterDrop() {
		return "keep: ret\t#-1\n" + 
				"drop: ret\t#0\n" + 
				"";
	}
	
	public static String binaryPredicateNonRecursive(String ethertype, String fieldOffset, String value){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld " +fieldOffset+ "\n" + 
				"jeq      " +value+ ", keep, drop\n";
	}
	
	public static String logicOrNonRecursive(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", keep, tf\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	
	public static String logicAndNonRecursive(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", tf, drop\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	
	public static String genIPethertype(Field field) {
		if (field.parent.equalsIgnoreCase("IPv6Header")){
			return "#0x" + Integer.toHexString(34525);
		} else {
			return "#0x" + Integer.toHexString(2048);
		}
	}
	
	public static String genIPethertype() {
		Field field = new Field("a", "IPv4", 10, 10);
		if (field.parent.equalsIgnoreCase("IPv6Header")){
			return "#0x" + Integer.toHexString(34525);
		} else {
			return "#0x" + Integer.toHexString(2048);
		}
	}
	
	//TODO
	
	public static String logicRecursive(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" +  "tr: ld       "+fieldOffset1+"\n" +  "jeq      "+value1+", keep, tf\n" + 
				"tf: ld       "+fieldOffset2+"\n" +  "jeq      "+value2+", keep, drop\n" +  "";
	}
	
	public static String test(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", keep, tf\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	
	
	public static String s1 = "next0l0:ldh      [12]\n" + 
			"next0l1: jeq      #0x800           , next0l2	, next0l4\n" + 
			"next0l2: ld       [26]\n" + 
			"next0l3: jeq      #0x1020304       , next1l0	, drop\n" + 
			"next0l4: jeq      #0x806           , next0l6	, next0l5\n" + 
			"next0l5: jeq      #0x8035          , next0l6	, drop\n" + 
			"next0l6: ld       [28]\n" + 
			"next0l7: jeq      #0x1020304       , next1l0	, drop\n" + 
			"next1l0: ldh      [12]\n" + 
			"next1l1: jeq      #0x800           , next1l2	, next1l4\n" + 
			"next1l2: ld       [30]\n" + 
			"next1l3: jeq      #0x1020304       , next2l0	, drop\n" + 
			"next1l4: jeq      #0x806           , next1l6	, next1l5\n" + 
			"next1l5: jeq      #0x8035          , next1l6	, drop\n" + 
			"next1l6: ld       [38]\n" + 
			"next1l7: jeq      #0x1020304       , next2l0	, drop\n" + 
			"next2l0: ldh      [12]\n" + 
			"next2l1: jeq      #0x86dd          , next2l2	, next2l8\n" + 
			"next2l2: ldb      [20]\n" + 
			"next2l3: jeq      #0x84            , next2l6	, next2l4\n" + 
			"next2l4: jeq      #0x6             , next2l6	, next2l5\n" + 
			"next2l5: jeq      #0x11            , next2l6	, drop\n" + 
			"next2l6: ldh      [54]\n" + 
			"next2l7: jeq      #0x3e8           , next3l0	, drop\n" + 
			"next2l8: jeq      #0x800           , next2l9	, drop\n" + 
			"next2l9: ldb      [23]\n" + 
			"next2l10: jeq      #0x84            , next2l13	, next2l11\n" + 
			"next2l11: jeq      #0x6             , next2l13	, next2l12\n" + 
			"next2l12: jeq      #0x11            , next2l13	, drop\n" + 
			"next2l13: ldh      [20]\n" + 
			"next2l14: jset     #0x1fff          , drop	, next2l15\n" + 
			"next2l15: ldxb     4*([14]&0xf)\n" + 
			"next2l16: ldh      [x + 14]\n" + 
			"next2l17: jeq      #0x3e8           , next3l0	, drop\n" + 
			"next3l0: ldh      [12]\n" + 
			"next3l1: jeq      #0x86dd          , next3l2	, next3l8\n" + 
			"next3l2: ldb      [20]\n" + 
			"next3l3: jeq      #0x84            , next3l6	, next3l4\n" + 
			"next3l4: jeq      #0x6             , next3l6	, next3l5\n" + 
			"next3l5: jeq      #0x11            , next3l6	, drop\n" + 
			"next3l6: ldh      [56]\n" + 
			"next3l7: jeq      #0x1770          , next4l0	, drop\n" + 
			"next3l8: jeq      #0x800           , next3l9	, drop\n" + 
			"next3l9: ldb      [23]\n" + 
			"next3l10: jeq      #0x84            , next3l13	, next3l11\n" + 
			"next3l11: jeq      #0x6             , next3l13	, next3l12\n" + 
			"next3l12: jeq      #0x11            , next3l13	, drop\n" + 
			"next3l13: ldh      [20]\n" + 
			"next3l14: jset     #0x1fff          , drop	, next3l15\n" + 
			"next3l15: ldxb     4*([14]&0xf)\n" + 
			"next3l16: ldh      [x + 16]\n" + 
			"next3l17: jeq      #0x1770          , next4l0	, drop\n" + 
			"next4l0: ldh      [12]\n" + 
			"next4l1: jeq      #0x800           , next4l2	, drop\n" + 
			"next4l2: ldb      [23]\n" + 
			"next4l3: jeq      #0x6             , next4l4	, drop\n" + 
			"next4l4: ldh      [20]\n" + 
			"next4l5: jset     #0x1fff          , drop	, next4l6\n" + 
			"next4l6: ldxb     4*([14]&0xf)\n" + 
			"next4l7: ld       [x + 18]\n" + 
			"next4l8: jeq      #0x5658b7e1      , drop	, next5l0\n" + 
			"next5l0: ldh      [12]\n" + 
			"next5l1: jeq      #0x800           , next5l2	, drop\n" + 
			"next5l2: ldb      [23]\n" + 
			"next5l3: jeq      #0x6             , next5l4	, drop\n" + 
			"next5l4: ldh      [20]\n" + 
			"next5l5: jset     #0x1fff          , drop	, next5l6\n" + 
			"next5l6: ldxb     4*([14]&0xf)\n" + 
			"next5l7: ld       [x + 22]\n" + 
			"next5l8: jeq      #0x2ebd4799      , drop	, next6l0\n" + 
			"next6l0: ldh      [12]\n" + 
			"next6l1: jeq      #0x800           , next6l2	, drop\n" + 
			"next6l2: ldb      [23]\n" + 
			"next6l3: jeq      #0x1             , next7l0	, drop\n" + 
			"next7l0: ldh      [12]\n" + 
			"next7l1: jeq      #0x800           , next7l2	, drop\n" + 
			"next7l2: ldb      [23]\n" + 
			"next7l3: jeq      #0x1             , next7l4	, drop\n" + 
			"next7l4: ldh      [20]\n" + 
			"next7l5: jset     #0x1fff          , drop	, next7l6\n" + 
			"next7l6: ldxb     4*([14]&0xf)\n" + 
			"next7l7: ldh      [x + 16]\n" + 
			"next7l8: jgt      #0x258           , next8l0	, drop\n" + 
			"next8l0: ldh      [12]\n" + 
			"next8l1: jeq      #0x800           , next8l2	, next8l6\n" + 
			"next8l2: ld       [26]\n" + 
			"next8l3: jeq      #0xa31f758       , next9l0	, next8l4\n" + 
			"next8l4: ld       [30]\n" + 
			"next8l5: jeq      #0xa010101       , next9l0	, drop\n" + 
			"next8l6: jeq      #0x806           , next8l8	, next8l7\n" + 
			"next8l7: jeq      #0x8035          , next8l8	, drop\n" + 
			"next8l8: ld       [28]\n" + 
			"next8l9: jeq      #0xa31f758       , next9l0	, next8l10\n" + 
			"next8l10: ld       [38]\n" + 
			"next8l11: jeq      #0xa010101       , next9l0	, drop\n" + 
			"next9l0: ldh      [12]\n" + 
			"next9l1: jeq      #0x800           , next9l2	, drop\n" + 
			"next9l2: ld       [30]\n" + 
			"next9l3: jeq      #0xae892a4d      , next10l0	, next9l4\n" + 
			"next9l4: ldb      [22]\n" + 
			"next9l5: jge      #0x3c            , drop	, next10l0\n" + 
			"next10l0: ldh      [12]\n" + 
			"next10l1: jeq      #0x800           , next10l2	, next10l6\n" + 
			"next10l2: ld       [26]\n" + 
			"next10l3: jeq      #0x1010101       , next10l4	, drop\n" + 
			"next10l4: ld       [30]\n" + 
			"next10l5: jeq      #0x1010101       , next11l0	, drop\n" + 
			"next10l6: jeq      #0x806           , next10l8	, next10l7\n" + 
			"next10l7: jeq      #0x8035          , next10l8	, drop\n" + 
			"next10l8: ld       [28]\n" + 
			"next10l9: jeq      #0x1010101       , next10l10	, drop\n" + 
			"next10l10: ld       [38]\n" + 
			"next10l11: jeq      #0x1010101       , next11l0	, drop\n" + 
			"next11l0: ldh      [12]\n" + 
			"next11l1: jeq      #0x800           , next11l2	, next11l4\n" + 
			"next11l2: ld       [30]\n" + 
			"next11l3: jeq      #0x1020304       , next12l0	, drop\n" + 
			"next11l4: jeq      #0x806           , next11l6	, next11l5\n" + 
			"next11l5: jeq      #0x8035          , next11l6	, drop\n" + 
			"next11l6: ld       [38]\n" + 
			"next11l7: jeq      #0x1020304       , next12l0	, drop\n" + 
			"next12l0: ldh      [12]\n" + 
			"next12l1: jeq      #0x86dd          , next12l2	, next12l8\n" + 
			"next12l2: ldb      [20]\n" + 
			"next12l3: jeq      #0x84            , next12l6	, next12l4\n" + 
			"next12l4: jeq      #0x6             , next12l6	, next12l5\n" + 
			"next12l5: jeq      #0x11            , next12l6	, drop\n" + 
			"next12l6: ldh      [54]\n" + 
			"next12l7: jeq      #0x3e8           , next13l0	, drop\n" + 
			"next12l8: jeq      #0x800           , next12l9	, drop\n" + 
			"next12l9: ldb      [23]\n" + 
			"next12l10: jeq      #0x84            , next12l13	, next12l11\n" + 
			"next12l11: jeq      #0x6             , next12l13	, next12l12\n" + 
			"next12l12: jeq      #0x11            , next12l13	, drop\n" + 
			"next12l13: ldh      [20]\n" + 
			"next12l14: jset     #0x1fff          , drop	, next12l15\n" + 
			"next12l15: ldxb     4*([14]&0xf)\n" + 
			"next12l16: ldh      [x + 14]\n" + 
			"next12l17: jeq      #0x3e8           , next13l0	, drop\n" + 
			"next13l0: ldh      [12]\n" + 
			"next13l1: jeq      #0x86dd          , next13l2	, next13l8\n" + 
			"next13l2: ldb      [20]\n" + 
			"next13l3: jeq      #0x84            , next13l6	, next13l4\n" + 
			"next13l4: jeq      #0x6             , next13l6	, next13l5\n" + 
			"next13l5: jeq      #0x11            , next13l6	, drop\n" + 
			"next13l6: ldh      [56]\n" + 
			"next13l7: jeq      #0x1770          , next14l0	, drop\n" + 
			"next13l8: jeq      #0x800           , next13l9	, drop\n" + 
			"next13l9: ldb      [23]\n" + 
			"next13l10: jeq      #0x84            , next13l13	, next13l11\n" + 
			"next13l11: jeq      #0x6             , next13l13	, next13l12\n" + 
			"next13l12: jeq      #0x11            , next13l13	, drop\n" + 
			"next13l13: ldh      [20]\n" + 
			"next13l14: jset     #0x1fff          , drop	, next13l15\n" + 
			"next13l15: ldxb     4*([14]&0xf)\n" + 
			"next13l16: ldh      [x + 16]\n" + 
			"next13l17: jeq      #0x1770          , next14l0	, drop\n" + 
			"next14l0: ldh      [12]\n" + 
			"next14l1: jeq      #0x800           , next14l2	, drop\n" + 
			"next14l2: ldb      [23]\n" + 
			"next14l3: jeq      #0x6             , next14l4	, drop\n" + 
			"next14l4: ldh      [20]\n" + 
			"next14l5: jset     #0x1fff          , drop	, next14l6\n" + 
			"next14l6: ldxb     4*([14]&0xf)\n" + 
			"next14l7: ld       [x + 18]\n" + 
			"next14l8: jeq      #0x5658b7e1      , drop	, next15l0\n" + 
			"next15l0: ldh      [12]\n" + 
			"next15l1: jeq      #0x800           , next15l2	, drop\n" + 
			"next15l2: ldb      [23]\n" + 
			"next15l3: jeq      #0x6             , next15l4	, drop\n" + 
			"next15l4: ldh      [20]\n" + 
			"next15l5: jset     #0x1fff          , drop	, next15l6\n" + 
			"next15l6: ldxb     4*([14]&0xf)\n" + 
			"next15l7: ld       [x + 22]\n" + 
			"next15l8: jeq      #0x2ebd4799      , drop	, next16l0\n" + 
			"next16l0: ldh      [12]\n" + 
			"next16l1: jeq      #0x800           , next16l2	, drop\n" + 
			"next16l2: ldb      [23]\n" + 
			"next16l3: jeq      #0x1             , next17l0	, drop\n" + 
			"next17l0: ldh      [12]\n" + 
			"next17l1: jeq      #0x800           , next17l2	, drop\n" + 
			"next17l2: ldb      [23]\n" + 
			"next17l3: jeq      #0x1             , next17l4	, drop\n" + 
			"next17l4: ldh      [20]\n" + 
			"next17l5: jset     #0x1fff          , drop	, next17l6\n" + 
			"next17l6: ldxb     4*([14]&0xf)\n" + 
			"next17l7: ldh      [x + 16]\n" + 
			"next17l8: jgt      #0x258           , next18l0	, drop\n" + 
			"next18l0: ldh      [12]\n" + 
			"next18l1: jeq      #0x800           , next18l2	, next18l6\n" + 
			"next18l2: ld       [26]\n" + 
			"next18l3: jeq      #0xa31f758       , next19l0	, next18l4\n" + 
			"next18l4: ld       [30]\n" + 
			"next18l5: jeq      #0xa010101       , next19l0	, drop\n" + 
			"next18l6: jeq      #0x806           , next18l8	, next18l7\n" + 
			"next18l7: jeq      #0x8035          , next18l8	, drop\n" + 
			"next18l8: ld       [28]\n" + 
			"next18l9: jeq      #0xa31f758       , next19l0	, next18l10\n" + 
			"next18l10: ld       [38]\n" + 
			"next18l11: jeq      #0xa010101       , next19l0	, drop\n" + 
			"next19l0: ldh      [12]\n" + 
			"next19l1: jeq      #0x800           , next19l2	, drop\n" + 
			"next19l2: ld       [30]\n" + 
			"next19l3: jeq      #0xae892a4d      , next2l0	, next19l4\n" + 
			"next19l4: ldb      [22]\n" + 
			"next19l5: jge      #0x3c            , drop	, next21l0\n" + 
			"next21l0: ldh      [12]\n" + 
			"next21l1: jeq      #0x800           , next21l2	, next21l4\n" + 
			"next21l2: ld       [30]\n" + 
			"next21l3: jeq      #0x1020304       , next22l0	, drop\n" + 
			"next21l4: jeq      #0x806           , next21l6	, next21l5\n" + 
			"next21l5: jeq      #0x8035          , next21l6	, drop\n" + 
			"next21l6: ld       [38]\n" + 
			"next21l7: jeq      #0x1020304       , next22l0	, drop\n" + 
			"next22l0: ldh      [12]\n" + 
			"next22l1: jeq      #0x86dd          , next22l2	, next22l8\n" + 
			"next22l2: ldb      [20]\n" + 
			"next22l3: jeq      #0x84            , next22l6	, next22l4\n" + 
			"next22l4: jeq      #0x6             , next22l6	, next22l5\n" + 
			"next22l5: jeq      #0x11            , next22l6	, drop\n" + 
			"next22l6: ldh      [54]\n" + 
			"next22l7: jeq      #0x3e8           , next23l0	, drop\n" + 
			"next22l8: jeq      #0x800           , next22l9	, drop\n" + 
			"next22l9: ldb      [23]\n" + 
			"next22l10: jeq      #0x84            , next22l13	, next22l11\n" + 
			"next22l11: jeq      #0x6             , next22l13	, next22l12\n" + 
			"next22l12: jeq      #0x11            , next22l13	, drop\n" + 
			"next22l13: ldh      [20]\n" + 
			"next22l14: jset     #0x1fff          , drop	, next22l15\n" + 
			"next22l15: ldxb     4*([14]&0xf)\n" + 
			"next22l16: ldh      [x + 14]\n" + 
			"next22l17: jeq      #0x3e8           , next23l0	, drop\n" + 
			"next23l0: ldh      [12]\n" + 
			"next23l1: jeq      #0x86dd          , next23l2	, next23l8\n" + 
			"next23l2: ldb      [20]\n" + 
			"next23l3: jeq      #0x84            , next23l6	, next23l4\n" + 
			"next23l4: jeq      #0x6             , next23l6	, next23l5\n" + 
			"next23l5: jeq      #0x11            , next23l6	, drop\n" + 
			"next23l6: ldh      [56]\n" + 
			"next23l7: jeq      #0x1770          , next24l0	, drop\n" + 
			"next23l8: jeq      #0x800           , next23l9	, drop\n" + 
			"next23l9: ldb      [23]\n" + 
			"next23l10: jeq      #0x84            , next23l13	, next23l11\n" + 
			"next23l11: jeq      #0x6             , next23l13	, next23l12\n" + 
			"next23l12: jeq      #0x11            , next23l13	, drop\n" + 
			"next23l13: ldh      [20]\n" + 
			"next23l14: jset     #0x1fff          , drop	, next23l15\n" + 
			"next23l15: ldxb     4*([14]&0xf)\n" + 
			"next23l16: ldh      [x + 16]\n" + 
			"next23l17: jeq      #0x1770          , next24l0	, drop\n" + 
			"next24l0: ldh      [12]\n" + 
			"next24l1: jeq      #0x800           , next24l2	, drop\n" + 
			"next24l2: ldb      [23]\n" + 
			"next24l3: jeq      #0x6             , next24l4	, drop\n" + 
			"next24l4: ldh      [20]\n" + 
			"next24l5: jset     #0x1fff          , drop	, next24l6\n" + 
			"next24l6: ldxb     4*([14]&0xf)\n" + 
			"next24l7: ld       [x + 18]\n" + 
			"next24l8: jeq      #0x5658b7e1      , drop	, next25l0\n" + 
			"next25l0: ldh      [12]\n" + 
			"next25l1: jeq      #0x800           , next25l2	, drop\n" + 
			"next25l2: ldb      [23]\n" + 
			"next25l3: jeq      #0x6             , next25l4	, drop\n" + 
			"next25l4: ldh      [20]\n" + 
			"next25l5: jset     #0x1fff          , drop	, next25l6\n" + 
			"next25l6: ldxb     4*([14]&0xf)\n" + 
			"next25l7: ld       [x + 22]\n" + 
			"next25l8: jeq      #0x2ebd4799      , drop	, next26l0\n" + 
			"next26l0: ldh      [12]\n" + 
			"next26l1: jeq      #0x800           , next26l2	, drop\n" + 
			"next26l2: ldb      [23]\n" + 
			"next26l3: jeq      #0x1             , next27l0	, drop\n" + 
			"next27l0: ldh      [12]\n" + 
			"next27l1: jeq      #0x800           , next27l2	, drop\n" + 
			"next27l2: ldb      [23]\n" + 
			"next27l3: jeq      #0x1             , next27l4	, drop\n" + 
			"next27l4: ldh      [20]\n" + 
			"next27l5: jset     #0x1fff          , drop	, next27l6\n" + 
			"next27l6: ldxb     4*([14]&0xf)\n" + 
			"next27l7: ldh      [x + 16]\n" + 
			"next27l8: jgt      #0x258           , next28l0	, drop\n" + 
			"next28l0: ldh      [12]\n" + 
			"next28l1: jeq      #0x800           , next28l2	, next28l6\n" + 
			"next28l2: ld       [26]\n" + 
			"next28l3: jeq      #0xa31f758       , next29l0	, next28l4\n" + 
			"next28l4: ld       [30]\n" + 
			"next28l5: jeq      #0xa010101       , next29l0	, drop\n" + 
			"next28l6: jeq      #0x806           , next28l8	, next28l7\n" + 
			"next28l7: jeq      #0x8035          , next28l8	, drop\n" + 
			"next28l8: ld       [28]\n" + 
			"next28l9: jeq      #0xa31f758       , next29l0	, next28l10\n" + 
			"next28l10: ld       [38]\n" + 
			"next28l11: jeq      #0xa010101       , next29l0	, drop\n" + 
			"next29l0: ldh      [12]\n" + 
			"next29l1: jeq      #0x800           , next29l2	, drop\n" + 
			"next29l2: ld       [30]\n" + 
			"next29l3: jeq      #0xae892a4d      , next30l0	, next29l4\n" + 
			"next29l4: ldb      [22]\n" + 
			"next29l5: jge      #0x3c            , drop	, next30l0\n" + 
			"next30l0:ldh      [12]\n" + 
			"next30l1: jeq      #0x800           , next30l2	, next30l4\n" + 
			"next30l2: ld       [26]\n" + 
			"next30l3: jeq      #0x1020304       , next31l0	, drop\n" + 
			"next30l4: jeq      #0x806           , next30l6	, next30l5\n" + 
			"next30l5: jeq      #0x8035          , next30l6	, drop\n" + 
			"next30l6: ld       [28]\n" + 
			"next30l7: jeq      #0x1020304       , next31l0	, drop\n" + 
			"next31l0: ldh      [12]\n" + 
			"next31l1: jeq      #0x800           , next31l2	, next31l4\n" + 
			"next31l2: ld       [30]\n" + 
			"next31l3: jeq      #0x1020304       , next32l0	, drop\n" + 
			"next31l4: jeq      #0x806           , next31l6	, next31l5\n" + 
			"next31l5: jeq      #0x8035          , next31l6	, drop\n" + 
			"next31l6: ld       [38]\n" + 
			"next31l7: jeq      #0x1020304       , next32l0	, drop\n" + 
			"next32l0: ldh      [12]\n" + 
			"next32l1: jeq      #0x86dd          , next32l2	, next32l8\n" + 
			"next32l2: ldb      [20]\n" + 
			"next32l3: jeq      #0x84            , next32l6	, next32l4\n" + 
			"next32l4: jeq      #0x6             , next32l6	, next32l5\n" + 
			"next32l5: jeq      #0x11            , next32l6	, drop\n" + 
			"next32l6: ldh      [54]\n" + 
			"next32l7: jeq      #0x3e8           , next33l0	, drop\n" + 
			"next32l8: jeq      #0x800           , next32l9	, drop\n" + 
			"next32l9: ldb      [23]\n" + 
			"next32l10: jeq      #0x84            , next32l13	, next32l11\n" + 
			"next32l11: jeq      #0x6             , next32l13	, next32l12\n" + 
			"next32l12: jeq      #0x11            , next32l13	, drop\n" + 
			"next32l13: ldh      [20]\n" + 
			"next32l14: jset     #0x1fff          , drop	, next32l15\n" + 
			"next32l15: ldxb     4*([14]&0xf)\n" + 
			"next32l16: ldh      [x + 14]\n" + 
			"next32l17: jeq      #0x3e8           , next33l0	, drop\n" + 
			"next33l0: ldh      [12]\n" + 
			"next33l1: jeq      #0x86dd          , next33l2	, next33l8\n" + 
			"next33l2: ldb      [20]\n" + 
			"next33l3: jeq      #0x84            , next33l6	, next33l4\n" + 
			"next33l4: jeq      #0x6             , next33l6	, next33l5\n" + 
			"next33l5: jeq      #0x11            , next33l6	, drop\n" + 
			"next33l6: ldh      [56]\n" + 
			"next33l7: jeq      #0x1770          , next34l0	, drop\n" + 
			"next33l8: jeq      #0x800           , next33l9	, drop\n" + 
			"next33l9: ldb      [23]\n" + 
			"next33l10: jeq      #0x84            , next33l13	, next33l11\n" + 
			"next33l11: jeq      #0x6             , next33l13	, next33l12\n" + 
			"next33l12: jeq      #0x11            , next33l13	, drop\n" + 
			"next33l13: ldh      [20]\n" + 
			"next33l14: jset     #0x1fff          , drop	, next33l15\n" + 
			"next33l15: ldxb     4*([14]&0xf)\n" + 
			"next33l16: ldh      [x + 16]\n" + 
			"next33l17: jeq      #0x1770          , next34l0	, drop\n" + 
			"next34l0: ldh      [12]\n" + 
			"next34l1: jeq      #0x800           , next34l2	, drop\n" + 
			"next34l2: ldb      [23]\n" + 
			"next34l3: jeq      #0x6             , next34l4	, drop\n" + 
			"next34l4: ldh      [20]\n" + 
			"next34l5: jset     #0x1fff          , drop	, next34l6\n" + 
			"next34l6: ldxb     4*([14]&0xf)\n" + 
			"next34l7: ld       [x + 18]\n" + 
			"next34l8: jeq      #0x5658b7e1      , drop	, next35l0\n" + 
			"next35l0: ldh      [12]\n" + 
			"next35l1: jeq      #0x800           , next35l2	, drop\n" + 
			"next35l2: ldb      [23]\n" + 
			"next35l3: jeq      #0x6             , next35l4	, drop\n" + 
			"next35l4: ldh      [20]\n" + 
			"next35l5: jset     #0x1fff          , drop	, next35l6\n" + 
			"next35l6: ldxb     4*([14]&0xf)\n" + 
			"next35l7: ld       [x + 22]\n" + 
			"next35l8: jeq      #0x2ebd4799      , drop	, next36l0\n" + 
			"next36l0: ldh      [12]\n" + 
			"next36l1: jeq      #0x800           , next36l2	, drop\n" + 
			"next36l2: ldb      [23]\n" + 
			"next36l3: jeq      #0x1             , next37l0	, drop\n" + 
			"next37l0: ldh      [12]\n" + 
			"next37l1: jeq      #0x800           , next37l2	, drop\n" + 
			"next37l2: ldb      [23]\n" + 
			"next37l3: jeq      #0x1             , next37l4	, drop\n" + 
			"next37l4: ldh      [20]\n" + 
			"next37l5: jset     #0x1fff          , drop	, next37l6\n" + 
			"next37l6: ldxb     4*([14]&0xf)\n" + 
			"next37l7: ldh      [x + 16]\n" + 
			"next37l8: jgt      #0x258           , next38l0	, drop\n" + 
			"next38l0: ldh      [12]\n" + 
			"next38l1: jeq      #0x800           , next38l2	, next38l6\n" + 
			"next38l2: ld       [26]\n" + 
			"next38l3: jeq      #0xa31f758       , next39l0	, next38l4\n" + 
			"next38l4: ld       [30]\n" + 
			"next38l5: jeq      #0xa010101       , next39l0	, drop\n" + 
			"next38l6: jeq      #0x806           , next38l8	, next38l7\n" + 
			"next38l7: jeq      #0x8035          , next38l8	, drop\n" + 
			"next38l8: ld       [28]\n" + 
			"next38l9: jeq      #0xa31f758       , next39l0	, next38l10\n" + 
			"next38l10: ld       [38]\n" + 
			"next38l11: jeq      #0xa010101       , next39l0	, drop\n" + 
			"next39l0: ldh      [12]\n" + 
			"next39l1: jeq      #0x800           , next39l2	, drop\n" + 
			"next39l2: ld       [30]\n" + 
			"next39l3: jeq      #0xae892a4d      , next40l0	, next39l4\n" + 
			"next39l4: ldb      [22]\n" + 
			"next39l5: jge      #0x3c            , drop	, next40l0\n" + 
			"next40l0:ldh      [12]\n" + 
			"next40l1: jeq      #0x800           , next40l2	, next40l4\n" + 
			"next40l2: ld       [26]\n" + 
			"next40l3: jeq      #0x1020304       , next41l0	, drop\n" + 
			"next40l4: jeq      #0x806           , next40l6	, next40l5\n" + 
			"next40l5: jeq      #0x8035          , next40l6	, drop\n" + 
			"next40l6: ld       [28]\n" + 
			"next40l7: jeq      #0x1020304       , next41l0	, drop\n" + 
			"next41l0: ldh      [12]\n" + 
			"next41l1: jeq      #0x800           , next41l2	, next41l4\n" + 
			"next41l2: ld       [30]\n" + 
			"next41l3: jeq      #0x1020304       , next42l0	, drop\n" + 
			"next41l4: jeq      #0x806           , next41l6	, next41l5\n" + 
			"next41l5: jeq      #0x8035          , next41l6	, drop\n" + 
			"next41l6: ld       [38]\n" + 
			"next41l7: jeq      #0x1020304       , next42l0	, drop\n" + 
			"next42l0: ldh      [12]\n" + 
			"next42l1: jeq      #0x86dd          , next42l2	, next42l8\n" + 
			"next42l2: ldb      [20]\n" + 
			"next42l3: jeq      #0x84            , next42l6	, next42l4\n" + 
			"next42l4: jeq      #0x6             , next42l6	, next42l5\n" + 
			"next42l5: jeq      #0x11            , next42l6	, drop\n" + 
			"next42l6: ldh      [54]\n" + 
			"next42l7: jeq      #0x3e8           , next43l0	, drop\n" + 
			"next42l8: jeq      #0x800           , next42l9	, drop\n" + 
			"next42l9: ldb      [23]\n" + 
			"next42l10: jeq      #0x84            , next42l13	, next42l11\n" + 
			"next42l11: jeq      #0x6             , next42l13	, next42l12\n" + 
			"next42l12: jeq      #0x11            , next42l13	, drop\n" + 
			"next42l13: ldh      [20]\n" + 
			"next42l14: jset     #0x1fff          , drop	, next42l15\n" + 
			"next42l15: ldxb     4*([14]&0xf)\n" + 
			"next42l16: ldh      [x + 14]\n" + 
			"next42l17: jeq      #0x3e8           , next43l0	, drop\n" + 
			"next43l0: ldh      [12]\n" + 
			"next43l1: jeq      #0x86dd          , next43l2	, next43l8\n" + 
			"next43l2: ldb      [20]\n" + 
			"next43l3: jeq      #0x84            , next43l6	, next43l4\n" + 
			"next43l4: jeq      #0x6             , next43l6	, next43l5\n" + 
			"next43l5: jeq      #0x11            , next43l6	, drop\n" + 
			"next43l6: ldh      [56]\n" + 
			"next43l7: jeq      #0x1770          , next44l0	, drop\n" + 
			"next43l8: jeq      #0x800           , next43l9	, drop\n" + 
			"next43l9: ldb      [23]\n" + 
			"next43l10: jeq      #0x84            , next43l13	, next43l11\n" + 
			"next43l11: jeq      #0x6             , next43l13	, next43l12\n" + 
			"next43l12: jeq      #0x11            , next43l13	, drop\n" + 
			"next43l13: ldh      [20]\n" + 
			"next43l14: jset     #0x1fff          , drop	, next43l15\n" + 
			"next43l15: ldxb     4*([14]&0xf)\n" + 
			"next43l16: ldh      [x + 16]\n" + 
			"next43l17: jeq      #0x1770          , next44l0	, drop\n" + 
			"next44l0: ldh      [12]\n" + 
			"next44l1: jeq      #0x800           , next44l2	, drop\n" + 
			"next44l2: ldb      [23]\n" + 
			"next44l3: jeq      #0x6             , next44l4	, drop\n" + 
			"next44l4: ldh      [20]\n" + 
			"next44l5: jset     #0x1fff          , drop	, next44l6\n" + 
			"next44l6: ldxb     4*([14]&0xf)\n" + 
			"next44l7: ld       [x + 18]\n" + 
			"next44l8: jeq      #0x5658b7e1      , drop	, next45l0\n" + 
			"next45l0: ldh      [12]\n" + 
			"next45l1: jeq      #0x800           , next45l2	, drop\n" + 
			"next45l2: ldb      [23]\n" + 
			"next45l3: jeq      #0x6             , next45l4	, drop\n" + 
			"next45l4: ldh      [20]\n" + 
			"next45l5: jset     #0x1fff          , drop	, next45l6\n" + 
			"next45l6: ldxb     4*([14]&0xf)\n" + 
			"next45l7: ld       [x + 22]\n" + 
			"next45l8: jeq      #0x2ebd4799      , drop	, next46l0\n" + 
			"next46l0: ldh      [12]\n" + 
			"next46l1: jeq      #0x800           , next46l2	, drop\n" + 
			"next46l2: ldb      [23]\n" + 
			"next46l3: jeq      #0x1             , next47l0	, drop\n" + 
			"next47l0: ldh      [12]\n" + 
			"next47l1: jeq      #0x800           , next47l2	, drop\n" + 
			"next47l2: ldb      [23]\n" + 
			"next47l3: jeq      #0x1             , next47l4	, drop\n" + 
			"next47l4: ldh      [20]\n" + 
			"next47l5: jset     #0x1fff          , drop	, next47l6\n" + 
			"next47l6: ldxb     4*([14]&0xf)\n" + 
			"next47l7: ldh      [x + 16]\n" + 
			"next47l8: jgt      #0x258           , next48l0	, drop\n" + 
			"next48l0: ldh      [12]\n" + 
			"next48l1: jeq      #0x800           , next48l2	, next48l6\n" + 
			"next48l2: ld       [26]\n" + 
			"next48l3: jeq      #0xa31f758       , next49l0	, next48l4\n" + 
			"next48l4: ld       [30]\n" + 
			"next48l5: jeq      #0xa010101       , next49l0	, drop\n" + 
			"next48l6: jeq      #0x806           , next48l8	, next48l7\n" + 
			"next48l7: jeq      #0x8035          , next48l8	, drop\n" + 
			"next48l8: ld       [28]\n" + 
			"next48l9: jeq      #0xa31f758       , next49l0	, next48l10\n" + 
			"next48l10: ld       [38]\n" + 
			"next48l11: jeq      #0xa010101       , next49l0	, drop\n" + 
			"next49l0: ldh      [12]\n" + 
			"next49l1: jeq      #0x800           , next49l2	, drop\n" + 
			"next49l2: ld       [30]\n" + 
			"next49l3: jeq      #0xae892a4d      , next50l0	, next49l4\n" + 
			"next49l4: ldb      [22]\n" + 
			"next49l5: jge      #0x3c            , drop	, next50l0\n" + 
			"next50l0:ldh      [12]\n" + 
			"next50l1: jeq      #0x800           , next50l2	, next50l4\n" + 
			"next50l2: ld       [26]\n" + 
			"next50l3: jeq      #0x1020304       , next51l0	, drop\n" + 
			"next50l4: jeq      #0x806           , next50l6	, next50l5\n" + 
			"next50l5: jeq      #0x8035          , next50l6	, drop\n" + 
			"next50l6: ld       [28]\n" + 
			"next50l7: jeq      #0x1020304       , next51l0	, drop\n" + 
			"next51l0: ldh      [12]\n" + 
			"next51l1: jeq      #0x800           , next51l2	, next51l4\n" + 
			"next51l2: ld       [30]\n" + 
			"next51l3: jeq      #0x1020304       , next52l0	, drop\n" + 
			"next51l4: jeq      #0x806           , next51l6	, next51l5\n" + 
			"next51l5: jeq      #0x8035          , next51l6	, drop\n" + 
			"next51l6: ld       [38]\n" + 
			"next51l7: jeq      #0x1020304       , next52l0	, drop\n" + 
			"next52l0: ldh      [12]\n" + 
			"next52l1: jeq      #0x86dd          , next52l2	, next52l8\n" + 
			"next52l2: ldb      [20]\n" + 
			"next52l3: jeq      #0x84            , next52l6	, next52l4\n" + 
			"next52l4: jeq      #0x6             , next52l6	, next52l5\n" + 
			"next52l5: jeq      #0x11            , next52l6	, drop\n" + 
			"next52l6: ldh      [54]\n" + 
			"next52l7: jeq      #0x3e8           , next53l0	, drop\n" + 
			"next52l8: jeq      #0x800           , next52l9	, drop\n" + 
			"next52l9: ldb      [23]\n" + 
			"next52l10: jeq      #0x84            , next52l13	, next52l11\n" + 
			"next52l11: jeq      #0x6             , next52l13	, next52l12\n" + 
			"next52l12: jeq      #0x11            , next52l13	, drop\n" + 
			"next52l13: ldh      [20]\n" + 
			"next52l14: jset     #0x1fff          , drop	, next52l15\n" + 
			"next52l15: ldxb     4*([14]&0xf)\n" + 
			"next52l16: ldh      [x + 14]\n" + 
			"next52l17: jeq      #0x3e8           , next53l0	, drop\n" + 
			"next53l0: ldh      [12]\n" + 
			"next53l1: jeq      #0x86dd          , next53l2	, next53l8\n" + 
			"next53l2: ldb      [20]\n" + 
			"next53l3: jeq      #0x84            , next53l6	, next53l4\n" + 
			"next53l4: jeq      #0x6             , next53l6	, next53l5\n" + 
			"next53l5: jeq      #0x11            , next53l6	, drop\n" + 
			"next53l6: ldh      [56]\n" + 
			"next53l7: jeq      #0x1770          , next54l0	, drop\n" + 
			"next53l8: jeq      #0x800           , next53l9	, drop\n" + 
			"next53l9: ldb      [23]\n" + 
			"next53l10: jeq      #0x84            , next53l13	, next53l11\n" + 
			"next53l11: jeq      #0x6             , next53l13	, next53l12\n" + 
			"next53l12: jeq      #0x11            , next53l13	, drop\n" + 
			"next53l13: ldh      [20]\n" + 
			"next53l14: jset     #0x1fff          , drop	, next53l15\n" + 
			"next53l15: ldxb     4*([14]&0xf)\n" + 
			"next53l16: ldh      [x + 16]\n" + 
			"next53l17: jeq      #0x1770          , next54l0	, drop\n" + 
			"next54l0: ldh      [12]\n" + 
			"next54l1: jeq      #0x800           , next54l2	, drop\n" + 
			"next54l2: ldb      [23]\n" + 
			"next54l3: jeq      #0x6             , next54l4	, drop\n" + 
			"next54l4: ldh      [20]\n" + 
			"next54l5: jset     #0x1fff          , drop	, next54l6\n" + 
			"next54l6: ldxb     4*([14]&0xf)\n" + 
			"next54l7: ld       [x + 18]\n" + 
			"next54l8: jeq      #0x5658b7e1      , drop	, next55l0\n" + 
			"next55l0: ldh      [12]\n" + 
			"next55l1: jeq      #0x800           , next55l2	, drop\n" + 
			"next55l2: ldb      [23]\n" + 
			"next55l3: jeq      #0x6             , next55l4	, drop\n" + 
			"next55l4: ldh      [20]\n" + 
			"next55l5: jset     #0x1fff          , drop	, next55l6\n" + 
			"next55l6: ldxb     4*([14]&0xf)\n" + 
			"next55l7: ld       [x + 22]\n" + 
			"next55l8: jeq      #0x2ebd4799      , drop	, next56l0\n" + 
			"next56l0: ldh      [12]\n" + 
			"next56l1: jeq      #0x800           , next56l2	, drop\n" + 
			"next56l2: ldb      [23]\n" + 
			"next56l3: jeq      #0x1             , next57l0	, drop\n" + 
			"next57l0: ldh      [12]\n" + 
			"next57l1: jeq      #0x800           , next57l2	, drop\n" + 
			"next57l2: ldb      [23]\n" + 
			"next57l3: jeq      #0x1             , next57l4	, drop\n" + 
			"next57l4: ldh      [20]\n" + 
			"next57l5: jset     #0x1fff          , drop	, next57l6\n" + 
			"next57l6: ldxb     4*([14]&0xf)\n" + 
			"next57l7: ldh      [x + 16]\n" + 
			"next57l8: jgt      #0x258           , next58l0	, drop\n" + 
			"next58l0: ldh      [12]\n" + 
			"next58l1: jeq      #0x800           , next58l2	, next58l6\n" + 
			"next58l2: ld       [26]\n" + 
			"next58l3: jeq      #0xa31f758       , next59l0	, next58l4\n" + 
			"next58l4: ld       [30]\n" + 
			"next58l5: jeq      #0xa010101       , next59l0	, drop\n" + 
			"next58l6: jeq      #0x806           , next58l8	, next58l7\n" + 
			"next58l7: jeq      #0x8035          , next58l8	, drop\n" + 
			"next58l8: ld       [28]\n" + 
			"next58l9: jeq      #0xa31f758       , next59l0	, next58l10\n" + 
			"next58l10: ld       [38]\n" + 
			"next58l11: jeq      #0xa010101       , next59l0	, drop\n" + 
			"next59l0: ldh      [12]\n" + 
			"next59l1: jeq      #0x800           , next59l2	, drop\n" + 
			"next59l2: ld       [30]\n" + 
			"next59l3: jeq      #0xae892a4d      , next60l0	, next59l4\n" + 
			"next59l4: ldb      [22]\n" + 
			"next59l5: jge      #0x3c            , drop	, next60l0\n" + 
			"next60l0:ldh      [12]\n" + 
			"next60l1: jeq      #0x800           , next60l2	, next60l4\n" + 
			"next60l2: ld       [26]\n" + 
			"next60l3: jeq      #0x1020304       , next61l0	, drop\n" + 
			"next60l4: jeq      #0x806           , next60l6	, next60l5\n" + 
			"next60l5: jeq      #0x8035          , next60l6	, drop\n" + 
			"next60l6: ld       [28]\n" + 
			"next60l7: jeq      #0x1020304       , next61l0	, drop\n" + 
			"next61l0: ldh      [12]\n" + 
			"next61l1: jeq      #0x800           , next61l2	, next61l4\n" + 
			"next61l2: ld       [30]\n" + 
			"next61l3: jeq      #0x1020304       , next62l0	, drop\n" + 
			"next61l4: jeq      #0x806           , next61l6	, next61l5\n" + 
			"next61l5: jeq      #0x8035          , next61l6	, drop\n" + 
			"next61l6: ld       [38]\n" + 
			"next61l7: jeq      #0x1020304       , next62l0	, drop\n" + 
			"next62l0: ldh      [12]\n" + 
			"next62l1: jeq      #0x86dd          , next62l2	, next62l8\n" + 
			"next62l2: ldb      [20]\n" + 
			"next62l3: jeq      #0x84            , next62l6	, next62l4\n" + 
			"next62l4: jeq      #0x6             , next62l6	, next62l5\n" + 
			"next62l5: jeq      #0x11            , next62l6	, drop\n" + 
			"next62l6: ldh      [54]\n" + 
			"next62l7: jeq      #0x3e8           , next63l0	, drop\n" + 
			"next62l8: jeq      #0x800           , next62l9	, drop\n" + 
			"next62l9: ldb      [23]\n" + 
			"next62l10: jeq      #0x84            , next62l13	, next62l11\n" + 
			"next62l11: jeq      #0x6             , next62l13	, next62l12\n" + 
			"next62l12: jeq      #0x11            , next62l13	, drop\n" + 
			"next62l13: ldh      [20]\n" + 
			"next62l14: jset     #0x1fff          , drop	, next62l15\n" + 
			"next62l15: ldxb     4*([14]&0xf)\n" + 
			"next62l16: ldh      [x + 14]\n" + 
			"next62l17: jeq      #0x3e8           , next63l0	, drop\n" + 
			"next63l0: ldh      [12]\n" + 
			"next63l1: jeq      #0x86dd          , next63l2	, next63l8\n" + 
			"next63l2: ldb      [20]\n" + 
			"next63l3: jeq      #0x84            , next63l6	, next63l4\n" + 
			"next63l4: jeq      #0x6             , next63l6	, next63l5\n" + 
			"next63l5: jeq      #0x11            , next63l6	, drop\n" + 
			"next63l6: ldh      [56]\n" + 
			"next63l7: jeq      #0x1770          , keep	, drop\n" + 
			"next63l8: jeq      #0x800           , next63l9	, drop\n" + 
			"next63l9: ldb      [23]\n" + 
			"next63l10: jeq      #0x84            , next63l13	, next63l11\n" + 
			"next63l11: jeq      #0x6             , next63l13	, next63l12\n" + 
			"next63l12: jeq      #0x11            , next63l13	, drop\n" + 
			"next63l13: ldh      [20]\n" + 
			"next63l14: jset     #0x1fff          , drop	, next63l15\n" + 
			"next63l15: ldxb     4*([14]&0xf)\n" + 
			"next63l16: ldh      [x + 16]\n" + 
			"next63l17: jeq      #0x1770          , keep	, drop\n" + 
			"keep: ret      #262144\n" + 
			"drop: ret      #0";
	
	public static String s2 ="next1l1: ldh [12]\n" + 
			"next1l2: jeq #0x800 , next1l3 , next1l7\n" + 
			"next1l3: ldx #26\n" + 
			"next1l4: ld #0xc0a80101\n" + 
			"next1l5: st [x+0]\n" + 
			"next1l6: ld [26]\n" + 
			"next1l7: jeq #0x806 , next1l9 , next1l8\n" + 
			"next1l8: jeq #0x8035 , next1l9 , keep\n" + 
			"next1l9: ldx #28\n" + 
			"next1l10: ld #0xc0a80101\n" + 
			"next1l11: st [x+0]\n" + 
			"next1l12: ld [28]\n" + 
			"keep: ret #262144\n" + 
			"drop: ret #0";
	public static String s3 ="next1l1: ldh [12]\n" + 
			"next1l2: jeq #0x800 , next1l3 , next1l7\n" + 
			"next1l3: ldx #30\n" + 
			"next1l4: ld #0xc620400c\n" + 
			"next1l5: st [x+0]\n" + 
			"next1l6: ld [26]\n" + 
			"next1l7: jeq #0x806 , next1l9 , next1l8\n" + 
			"next1l8: jeq #0x8035 , next1l9 , keep\n" + 
			"next1l9: ldx #38\n" + 
			"next1l10: ld #0xc620400c\n" + 
			"next1l11: st [x+0]\n" + 
			"next1l12: ld [28]\n" + 
			"keep: ret #262144\n" + 
			"drop: ret #0";
	public static String s4 = "next1l1: ldh [12]\n" + 
			"next1l2: jeq #0x800 , next1l3 , next1l7\n" + 
			"next1l3: ldx #26\n" + 
			"next1l4: ld #0xc0a80101\n" + 
			"next1l5: st [x+0]\n" + 
			"next1l6: ld [26]\n" + 
			"next1l7: jeq #0x806 , next1l9 , next1l8\n" + 
			"next1l8: jeq #0x8035 , next1l9 , next2l1\n" + 
			"next1l9: ldx #28\n" + 
			"next1l10: ld #0xc0a80101\n" + 
			"next1l11: st [x+0]\n" + 
			"next1l12: ld [28]\n" + 
			"next2l1: ldh [12]\n" + 
			"next2l2: jeq #0x800 , next2l3 , next2l7\n" + 
			"next2l3: ldx #30\n" + 
			"next2l4: ld #0xc620400c\n" + 
			"next2l5: st [x+0]\n" + 
			"next2l6: ld [26]\n" + 
			"next2l7: jeq #0x806 , next2l9 , next2l8\n" + 
			"next2l8: jeq #0x8035 , next2l9 , keep\n" + 
			"next2l9: ldx #38\n" + 
			"next2l10: ld #0xc620400c\n" + 
			"next2l11: st [x+0]\n" + 
			"next2l12: ld [28]\n" + 
			"keep: ret #262144\n" + 
			"drop: ret #0";	
	public static String s5 ="next1l1: ldh [12]\n" + 
			"next1l2: jeq #0x86dd , next2l1 , next1l3\n" + 
			"next1l3: jeq #0x800 , next1l4 , next2l1\n" + 
			"next1l4: ldb [23]\n" + 
			"next1l5: jeq #0x11 , next1l6 , next2l1\n" + 
			"next1l6: ldx #26\n" + 
			"next1l7: ld #0xa24e003\n" + 
			"next1l8: st [x+0]\n" + 
			"next1l9: ld [26]\n" + 
			"next2l1: ldh [12]\n" + 
			"next2l2: jeq #0x86dd , keep , next2l3\n" + 
			"next2l3: jeq #0x800 , next2l4 , keep\n" + 
			"next2l4: ldb [23]\n" + 
			"next2l5: jeq #0x6 , next2l6 , keep\n" + 
			"next2l6: ldx #30\n" + 
			"next2l7: ld #0xc620400c\n" + 
			"next2l8: st [x+0]\n" + 
			"next2l9: ld [30]\n" + 
			"keep: ret #262144\n" + 
			"drop: ret #0";
	public static String s6 ="next1l1: ldh [12]\n" + 
			"next1l2: jeq #0x86dd , next1l3 , next1l8\n" + 
			"next1l3: ldb [20]\n" + 
			"next1l4: jeq #0x6 , next2l1 , next1l5\n" + 
			"next1l5: jeq #0x2c , next1l6 , drop\n" + 
			"next1l6: ldb [54]\n" + 
			"next1l7: jeq #0x6 , next2l1 , drop\n" + 
			"next1l8: jeq #0x800 , next1l9 , drop\n" + 
			"next1l9: ldb [23]\n" + 
			"next1l10: jeq #0x6 , next2l1 , drop\n" + 
			"next2l1: ldh [12]\n" + 
			"next2l2: jeq #0x800 , next2l3 , next2l5\n" + 
			"next2l3: ld [30]\n" + 
			"next2l4: jeq #0xa31f758 , next3l1 , drop\n" + 
			"next2l5: jeq #0x806 , next2l7 , next2l6\n" + 
			"next2l6: jeq #0x8035 , next2l7 , drop\n" + 
			"next2l7: ld [38]\n" + 
			"next2l8: jeq #0xa31f758 , next3l1 , drop\n" + 
			"next3l1: ldh [12]\n" + 
			"next3l2: jeq #0x800 , next3l3 , next3l7\n" + 
			"next3l3: ldx #26\n" + 
			"next3l4: ld #0xc0a80101\n" + 
			"next3l5: st [x+0]\n" + 
			"next3l6: ld [26]\n" + 
			"next3l7: jeq #0x806 , next3l9 , next3l8\n" + 
			"next3l8: jeq #0x8035 , next3l9 , next4l1\n" + 
			"next3l9: ldx #28\n" + 
			"next3l10: ld #0xc0a80101\n" + 
			"next3l11: st [x+0]\n" + 
			"next3l12: ld [28]\n" + 
			"next4l1: ldh [12]\n" + 
			"next4l2: jeq #0x800 , next4l3 , next4l7\n" + 
			"next4l3: ldx #30\n" + 
			"next4l4: ld #0xc0a80101\n" + 
			"next4l5: st [x+0]\n" + 
			"next4l6: ld [26]\n" + 
			"next4l7: jeq #0x806 , next4l9 , next4l8\n" + 
			"next4l8: jeq #0x8035 , next4l9 , keep\n" + 
			"next4l9: ldx #38\n" + 
			"next4l10: ld #0xc0a80101\n" + 
			"next4l11: st [x+0]\n" + 
			"next4l12: ld [28]\n" + 
			"keep: ret #262144\n" + 
			"drop: ret #0";
}
