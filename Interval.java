public class Interval{
	public static final String LOWER_LIMIT = "bmphuxdzdptmwfjkmswpmglqaugolcfkigzilpezvauvsmzgfaouwkocudbtikcflwtefzrfogttksokdbqmxlypejbeav";
	public static final String UPPER_LIMIT = "ljgqqmsfxichjlmffquellmckrpxejrixcacoidudhecpekusbfailponelrjsladcwypghpolmwyefizrwfsujfinwqqblqrd";
	public static boolean testMode = false;	
	public static boolean testDupMode = false;

	//the following two arrays are parrallel its sloppy code I know sorry.
	public static final String[] TEST_KEYS_IN_ORDER =	
	{
			// search in range should result with keys starting here
			// "java mydbtest (1,2 or 3) test"
			// search option and use 'lower limit' and 'upper limit'
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
			"yuutfxsweqvxfzktwvnmmiwpyqztdhplldnidfzckznqlscpbgktxplcmxhmuepeymygpooyxruacgcjpeyloxczpgetudekngrrcyujluqm"
	};
	
	public static final String[] TEST_DATA = 
	{
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
			"rallaikxupuskmmoentxqbnzjrinoczmjkwqpaoiirggmmoihdfhlumsnbnkqunawgr"
	};

	/* I should of used a matrix in the first place instead of those shitty parrallel arrays
	 * first element is key. second is data.
   * I'm not going to use random generation, because I want to test duplicate data specifcally 
  */
	public static final String[][] DUP_TEST_MATRIX = {
		{"key1", "duplicate data"},
    {"key2", "duplicate data"},
		{"key3","chips"},
		{"key4","duplicate data"},
		{"key5","pop"},
	};

	public static final String[][] getTestSecondary(){
		String[][] secondaryModel = new String [Interval.TEST_DATA.length][2];
		for (int i = 0; i < Interval.TEST_DATA.length; i++){
			secondaryModel[i][0] = Interval.TEST_DATA[i];
			secondaryModel[i][1] = Interval.TEST_KEYS_IN_ORDER[i];
		}
		return secondaryModel;
	}																			
	
	public static void setTestDupMode(){
		testDupMode = true;
	}									

	public static void setTestMode(){
		testMode = true;
	}
}












