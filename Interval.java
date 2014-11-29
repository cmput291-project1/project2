public class Interval{
	public static final String LOWER_LIMIT = "aaaapjjknhqcfdaoxdbghixrpkjnvautkjsjkqqbxhcgbgozowkpldjqnwfkhbveggqkbgtrnwruakhumnwguck";
	public static final String UPPER_LIMIT = "aaqumsxzjosfiqvtvtrnpncocpfcspahelkqvhraitrpcsmktpcvhdvjkzxozunocdoesndqffqqmjlrkfshnnpvzjycymnbtvrljhytirvpfzmwvnwfsmxuvoumlel";
	public static final String TEST_LOWER_LIMIT = "a";
	public static final String TEST_UPPER_LIMIT = "d";
	public static boolean testMode = false;	

	//the following two arrays are parrallel its sloppy code I know sorry.
	public static final String[] TEST_KEYS_IN_ORDER =	{
			"bmphuxdzdptmwfjkmswpmglqaugolcfkigzilpezvauvsmzgfaouwkocudbtikcflwtefzrfogttksokdbqmxlypejbeav",
			"fmjdpaoxznhtzspvskxrotiqtpjigoarswzoxufdvhfwrmlbdoeevnwgkuqjqubpfgaw",
			"gjgsonbadpzwyotrsaujnvtpesmtkwzrrnnppyzpbprujltwfaqiemvrkyfnkpsrmikyzagjvoenheuyoalwadrdathmnuumgp",
			"ihczrdenfjcewdzbykvlalzzrblvmzbeqrlsaprcwnsmxjpqwtpytrijstriqzvukhnilao",
			"ljgqqmsfxichjlmffquellmckrpxejrixcacoidudhecpekusbfailponelrjsladcwypghpolmwyefizrwfsujfinwqqblqrd",
			"mbtufnatyvmgcmvymdwowlstsugowsjdbozlxcddvykfavtlywxltmboltwquuyqcqjbtkwnxmniqugajd",
			"nvxwhkdnjoiedwinonvgkuhteabclodtfggmokcaaiqukxnvbijucjfqxemeueblpgfshztpkckhbfkznlaasexsbazmgtalefxchsywhlbh",
			"oohiqwurgzsllzvhgigpxqwzbenyyjxuczmewrecjmxuvgjlzrnfxlmgzoilphatfquyyaadzvnztflneudhykt",
			"talivnovdmzkugxtmsofewhfqkaqeodwxstcptsioiorzhuqucpygfhonelfhtstvwukaqduccwbyjybevqgmjnptattrjyfdmiipkudkuumprptfdllymfmmpija",
			"twevyvmakazvpgojbxuxhettsluktqcontnwptygtybimlosnbzoblhfeysxhakxwfey",
			"yuutfxsweqvxfzktwvnmmiwpyqztdhplldnidfzckznqlscpbgktxplcmxhmuepeymygpooyxruacgcjpeyloxczpgetudekngrrcyujluqm"};
	
	public static final String[] TEST_DATA = {
			"lxqohkaipnfqolcvcpuasjhkuyogqyilbtiygwpwpnvrsfvgxgqmoknslwahseqgtukfojzfrvpennfvcsrasydam",
			"jwdnhunkwfkrsfifctqojgjpayvgyjmdxvbwtolkvfkfyqpzvmmrihifaxrmzuernvnmtb",
			"aoziejtrltommnlbygibgdruxofazvlafzufoshmzyisyavqabqlothihdkuuquwlyhaywkokbatasbxturcjgypqxosjnz",
			"fruenvdbxxwchlspuojntecjvolgoxbatcrpfguthpwzpcqzyzypmaiponleumfwxrtmysuspefx",
			"ypgncqqsbfdsqkdsjkwedsylaemsdjixeexjhbizztusifwhagtnfpxmmdcowygomiybpkynkjaivdevloyqdszlybgqhffp",
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












