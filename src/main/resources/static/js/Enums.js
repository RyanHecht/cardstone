const ZoneEnum = {
  HAND: 1,
  AURA: 2,
  CREATURE: 3,
};


const TypeEnum = {
	CREATURE: 1,
	SPELL: 2,
	BACK: 3,
	ELEMENT: 4,
};

const ColorEnum = {
	DEFAULT:0,
	RANGED:1,
	MONO:2
};

const AnimationEnum = {
	RADIAL:0,
	CLOUD:1
};

const StateEnum = {
	IDLE:0,
	TARGET_NEEDED:1,
	CHOICE_NEEDED:2  
}

const WIDTH_RATIO = .7;
const STAT_ALPHA = .8;
const UPDATE_RATE = 25;

const fire = {r : 106, g : 4, b : 30};
const water = {r : 128, g : 188, b : 163};
const earth = {r : 130, g : 99, b : 66};
const air = {r : 246, g : 247, b : 189};

const fireText = "rgba(150,29,40," + STAT_ALPHA  + ")";
const waterText = "rgba(42,143,189," + STAT_ALPHA + ")";
const earthText = "rgba(191,167,111," + (STAT_ALPHA ) + ")";
const airText = "rgba(201,255,227," + STAT_ALPHA + ")";
const balanceText = "rgba(211,211,211," + (STAT_ALPHA) +  ")";
const DEFAULT_ANIM_COLOR = "rgba(100,100,100,1.0)";
const MAX_HEALTH = 30;