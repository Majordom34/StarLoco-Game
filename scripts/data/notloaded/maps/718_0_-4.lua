local map = MapDef(
	718,
	"0706131721",
	"47502D43222C3D6A36716051223D68666A3A7269332D6E7D68707E77592F602532352038316E595E5D6077777A5443446920757C684860582C2F463D6C272D347B3E287A646A76494925323524346D477941287A57524D2E76494E645A7A2A4E242532356E60384D484E7D4F2C3C7671406B662D3B5A68342532355E2F782D203B4D6F407737795F5F52456C7E2D5E374621684F6E2473463E7A44467370464D7E774E66665B785E2D6B23634B2C2F38507765683D5E312C414B3651274C4857426E577C4D2E3F624D66733A646B3C663A57337C242532354F42386C6C6730667E453839663B4C34",
	"6a445c0b53100130435c200e0b5b1708524c0f1c20181f163c4e014441597906383f380116161b350b2c0841101d092901396447275c09464c551a5f6012050b132828444555252f18204d1b36332c4f3e212f053f1b4b2f45442608592c2d2f1c2e4d5d3e19210a034c5a3b09556d364e1948415a2c0e213f5f183e3a33240d1f4c165f27400d2e0f451227761225271611272c1f16060e073a1d3f4c0a420203444e59351604095c3f7944202a5330462d29360a06361d284f5e032c073b52050a59075b36521d6c4d2e235d0d0d065107362d5958035a2d552631652b434d580b571001306a550a010f5b1308524c261509111b16384e01446850500f3c3f3c011616323c22250c41141d092928304d4e235c0d464c5533564a1d010b172828446c5c0f201c20491b3633054617282b0e681b4b2f6c4d0c075d2c292f1c2e64541710250a074c5a3b205c443f4a194c415a2c27283f501c2e3833240d364516502340092e0f453b2e5c1d21271211272c361f2c01033a193f4c0a6b0b2a4d4a59311604097536534b24215e30462d003f0a09321d2c4f5e03050e3b5d010a5d075b367b146c422a335f0d0d06780e1f245d58075a2d550f384c2247460c0b571028396a5a0d070b5b13087b45261a0d111f16384e284d685f540f383f3c013f1f1833262e0149141d2020023f494e275c0d46655c19594d0b2a0b1c2b014d6c5308261820491b1f3a054913282f053b1b62266c420b01592c292f35274e5b131b7102074c73320a53403f4e194c41732527271256183e3e330d04364a3b562740092e264c3b215b1b252712110e2536102b17283a193f650341042e461c5131162d007539545d0f2a57306f240030270f131d2c4f770a0501165b050a5d07723f7b1b41540123590d240f78011b347658075a045c253748326c4d5c0b7e192836475c09070b5b3a017b4a0b1c09111f16114728424559500f383f15083f101f250d2508413d140a2f05394d4e275c244f65531e4f4f1b050b3e21014241550c26182060121f35284f17282f051212622941540901592c00263528494d3810210a2e45593d0d55443f4e196548592a0a211656183e173a270b1b4c3f56274020270c4316275f1b25273b18242a1b162f07073a30364f0c46022a4d4e59",
	6,
	8,
	0,
	-4,
	29
)

map.positions = "aJaOaPaTaUaZa4a9|a1a6a7a_babebfbk"
map.mobGroupsCount = 3
map.mobGroupsMinSize = 8

-- '0;0;0;0;0;0;0' forbiddens -> capabilities ? Or script ?

map.onMovementEnd = {
	[34] = moveEndTeleport(720, 69),
	[42] = moveEndTeleport(702, 67),
	[72] = moveEndTeleport(721, 47),
	[75] = moveEndTeleport(719, 40),
}

RegisterMapDef(map)