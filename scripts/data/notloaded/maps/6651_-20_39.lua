local map = MapDef(
	6651,
	"0706131721",
	"352532356864397B66242A49676F5A41485A2371292A237D5A595F367A253235394B3F517C476E7870717A6A41706F4B41484E386F32325D2532625533504B5F5247487E7B333B747376736E7C20515778622023613E54474D2E79327C4145786B535778604F477D6B6D4D633E654D422825323579405D3E6D2A67324C2232305E317831665D6B744F4245575122266834326C32332438534B4B6C7D767C222E34297C4C6C2D686E45736D4E3D3F273771224C3C2F777B36502E75574E78",
	"bhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhaaeogaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhaaeaaaaaHhaaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhaaeofaaaGhaaeaaaxOHhaaeofiaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhaaeofaaaHhaaeaaaaaHhaaeaaaaaHhaaeofiaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaxFHhaaeofaaaGhaaeaaaxOHhGmeoIaaaGhaaeaaaxTHhaaeofiaabhGaeaaexFbhGaeaaaaabhGaeaaaaabhGaeaaaaaHhaaeofaaaHhaaeaaaaaHhGaeoIaaaHhGaeaaaaaGhaaeaaaxQHhaaeofiaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhaaeofaaaHhqaeqgahFHhGmeaaaaaHhaaeoJahYHhGmeaaaaaHhaaeaaaqXHhaaeofiaabhGaeaaaaabhGaeaaaaaHhaaeofaaaHhGaeaaaaaHhGaeaaaaaHhaaeaaahYGhaaeaaaxKHhGaeaaaaaHhGaeaaaaaHhaaeofiaabhGaeaaaaaHhaaeofaaaHhaaeaaenFHhGmeaaaaaHhGaeaaaaaHhameqYaaaHhaaeaaahYHhGmeaaaaaHhGaeaaaaaHhaaeaaaaaHhaaeofaaaGhaaeaaaxIHhGaeaaaaaHhGaeaaaaaGhaaeaaaxKHhGaeaaaaaHhGaeaaaaaHhGmeaaaaaHhcpeoIaaaHhaaeofaaaHhaaeaaaaaHhameaaanHHhGaeaaaaaHhameaaaaaHhGaeaaaaaHhGmeaaaaaHhGaeaaaaaHhcpeaaaaabhGaeaaaaaHhaaeaaaaaHhGaeoJaaaHhGaeaaaaaHhGaeaaaaaHhaaeaaahYHhGaeaaaaaHhGmeaaaaaHhcpeoJaaabhGaeaaaaaHhcoeaaaaaHhGmeaaaaaHhGaemWaaaHhGmeaaaaaHhGaeaaaaaHhGmeaaaaaHhGaeaaaaaHhcpeaaaaabhGaeaaaaabhGaeaaaaaHhcoeaaaaaHhGaeaaaaaHhGaeaaaaaHhGaeaaaaaHhGaeaaaaaHhGmeaaaaaHhr7eqgaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhcoeaaaaaHhameaaaaaHhGaeaaaaaHhGmeaaaaaHhGaeaaaaaHhcpeoIaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhcoeaaaaaHhiaeaahTLGhaaeaaaxHHhGmeaaaaaHhcpeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhcoeaaaaaGhameaaaxHGhaaeaaaxHHhcpeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhcoeaaaaaGhameaaaxHHhcpeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhcoeaaaaaHhcpeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaaHhaaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaabhGaeaaaaa",
	10,
	14,
	-20,
	39,
	53
)

map.positions = "cdcvcwcNcOc5c6dm|b-b_crcscJcKcLc2"
map.mobGroupsCount = 3
map.mobGroupsMinSize = 8

-- '0;0;0;0;0;0;0' forbiddens -> capabilities ? Or script ?

map.onMovementEnd = {
	[117] = moveEndTeleport(6653, 107),
	[187] = moveEndTeleport(4613, 264),
}

RegisterMapDef(map)