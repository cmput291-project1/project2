public class Interval{
	public static final String LOWER_LIMIT = "bmphuxdzdptmwfjkmswpmglqaugolcfkigzilpezvauvsmzgfaouwkocudbtikcflwtefzrfogttksokdbqmxlypejbeav";
	public static final String UPPER_LIMIT = "ljgqqmsfxichjlmffquellmckrpxejrixcacoidudhecpekusbfailponelrjsladcwypghpolmwyefizrwfsujfinwqqblqrd";
	public static boolean testMode = false;	

	//the following two arrays are parrallel its sloppy code I know sorry.
	public static final String[] TEST_KEYS_IN_ORDER =	{
			// search in range should result with keys starting here
			// "java mydbtest (1,2 or 3) test"
			"bmphuxdzdptmwfjkmswpmglqaugolcfkigzilpezvauvsmzgfaouwkocudbtikcflwtefzrfogttksokdbqmxlypejbeav",
			"fmjdpaoxznhtzspvskxrotiqtpjigoarswzoxufdvhfwrmlbdoeevnwgkuqjqubpfgaw",
			"gjgsonbadpzwyotrsaujnvtpesmtkwzrrnnppyzpbprujltwfaqiemvrkyfnkpsrmikyzagjvoenheuyoalwadrdathmnuumgp",
			"ihczrdenfjcewdzbykvlalzzrblvmzbeqrlsaprcwnsmxjpqwtpytrijstriqzvukhnilao",
			"ljgqqmsfxichjlmffquellmckrpxejrixcacoidudhecpekusbfailponelrjsladcwypghpolmwyefizrwfsujfinwqqblqrd",
			// and end here
			"mbtufnatyvmgcmvymdwowlstsugowsjdbozlxcddvykfavtlywxltmboltwquuyqcqjbtkwnxmniqugajd",
			"nvxwhkdnjoiedwinonvgkuhteabclodtfggmokcaaiqukxnvbijucjfqxemeueblpgfshztpkckhbfkznlaasexsbazmgtalefxchsywhlbh",
			"oohiqwurgzsllzvhgigpxqwzbenyyjxuczmewrecjmxuvgjlzrnfxlmgzoilphatfquyyaadzvnztflneudhykt",
			"talivnovdmzkugxtmsofewhfqkaqeodwxstcptsioiorzhuqucpygfhonelfhtstvwukaqduccwbyjybevqgmjnptattrjyfdmiipkudkuumprptfdllymfmmpija",
			"twevyvmakazvpgojbxuxhettsluktqcontnwptygtybimlosnbzoblhfeysxhakxwfey",
			"yuutfxsweqvxfzktwvnmmiwpyqztdhplldnidfzckznqlscpbgktxplcmxhmuepeymygpooyxruacgcjpeyloxczpgetudekngrrcyujluqm"};
	
	public static final String[] TEST_DATA = {
			// search in range should result with keys starting here
			// "java mydbtest (1,2 or 3) test"
			"lxqohkaipnfqolcvcpuasjhkuyogqyilbtiygwpwpnvrsfvgxgqmoknslwahseqgtukfojzfrvpennfvcsrasydam",
			"jwdnhunkwfkrsfifctqojgjpayvgyjmdxvbwtolkvfkfyqpzvmmrihifaxrmzuernvnmtb",
			"aoziejtrltommnlbygibgdruxofazvlafzufoshmzyisyavqabqlothihdkuuquwlyhaywkokbatasbxturcjgypqxosjnz",
			"fruenvdbxxwchlspuojntecjvolgoxbatcrpfguthpwzpcqzyzypmaiponleumfwxrtmysuspefx",
			"ypgncqqsbfdsqkdsjkwedsylaemsdjixeexjhbizztusifwhagtnfpxmmdcowygomiybpkynkjaivdevloyqdszlybgqhffp",
			// and end here
			"gccobqxovgodchmflxpcwabfhxkrdphtkuusvziuonupvicbctesenvuvdszgrglbyafiaiuwzroziokjqofjbygsaqhrdanhfuucgkcnfmpc",
			"mliqrsbnljazdbvduwuxqpkvccufhegopsvbluemyhonwfofxvdterowxyqapmqgmtebdxpfqvfnsqcogmytahzuwuouglmyfkikmcscjimbdnhx",
			"hnrkmhfcwwzplsykkcaxqdtsnlenyanztgszjgnzvdgpidenkicynfdsgeyvgbnbaxdxlodtwexdlp",
			"mtybnuerfienrryvavalidhqyyxmwtgotmefcwsegellkmrqehkzadfsffonqfcphkhiflxietfkjvpixixzrldaghjrwfvfmakhkdajj",
			"ldnnaqolvixmgucmfendjwalgxcivpfcnvfeltnkjkltwpuklpajwanbniyktgvronlnvlurbxukuasqenhi",
			"rallaikxupuskmmoentxqbnzjrinoczmjkwqpaoiirggmmoihdfhlumsnbnkqunawgr"};

	public static final String[][] getTestSecondary(){
		String[][] secondaryModel = new String [Interval.TEST_DATA.length][2];
		for (int i = 0; i < Interval.TEST_DATA.length; i++){
			secondaryModel[i][0] = Interval.TEST_DATA[i];
			secondaryModel[i][1] = Interval.TEST_KEYS_IN_ORDER[i];
		}
		return secondaryModel;
	}																			
	
										

	public static void setTestMode(){
		testMode = true;
	}
}












