local group1 = {
    {47, {1, 2, 3, 4, 5}},
    {652, {1, 2, 3, 4, 5}},
    {653, {1, 2, 3, 4, 5}}
}

local group2 = {
    {47, {1, 2, 3, 4, 5}},
    {47, {1, 2, 3, 4, 5}},
    {256, {1, 2, 3, 4, 5}},
    {256, {1, 2, 3, 4, 5}}
}

local group3 = {
    {989, {1, 2, 3, 4, 5}}
}

local group4 = {
    {990, {1, 2, 3, 4, 5}},
    {652, {1, 2, 3, 4, 5}}
}

local group5 = {
    {254, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}}
}

local group6 = {
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}}
}

local group7 = {
    {254, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {652, {1, 2, 3, 4, 5}},
    {652, {1, 2, 3, 4, 5}},
    {652, {1, 2, 3, 4, 5}},
    {652, {1, 2, 3, 4, 5}}
}

local group8 = {
    {254, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}}
}

local group9 = {
    {253, {1, 2, 3, 4, 5}},
    {256, {1, 2, 3, 4, 5}}
}

local group10 = {
    {253, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {254, {1, 2, 3, 4, 5}},
    {47, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}}
}

local group11 = {
    {173, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {917, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}},
    {650, {1, 2, 3, 4, 5}}
}

local mapInfos = {
    [8713] = {groupCell = 238, group = group1, winDest = {8709, 112}},
    [8709] = {groupCell = 253, group = group2, winDest = {8715, 119}},
    [8716] = {groupCell = 310, group = group3, winDest = {8718, 439}},
    [8718] = {groupCell = 97, group = group4, winDest = {8719, 207}},
    [8721] = {groupCell = 338, group = group5, winDest = {8722, 422}},
    [8722] = {groupCell = 185, group = group6, winDest = {8723, 67}},
    [8723] = {groupCell = 210, group = group7, winDest = {8724, 376}},
    [8724] = {groupCell = 224, group = group8, winDest = {8710, 147}},
    [8710] = {groupCell = 281, group = group9, winDest = {8711, 423}},
    [8711] = {groupCell = 254, group = group10, winDest = {10234, 437}},
    [10234] = {groupCell = 225, group = group11, winDest = {10235, 437}}
}

for k, v in pairs(mapInfos) do
    local map = MAPS[k]
    if map then
        map.onFightEnd[PVMFightType] = fightEndTeleportWinnerPlayer(v.winDest[1], v.winDest[2])
        map.staticGroups = {
            {v.groupCell, v.group}
        }
    end
end
