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
const STAT_ALPHA = .52;
const UPDATE_RATE = 25;

const fire = {r : 200, g : 15, b : 0};
const water = {r : 128, g : 188, b : 163};
const earth = {r : 130, g : 99, b : 66};
const air = {r : 246, g : 247, b : 189};

const fireText = "rgba(200,15,0," + STAT_ALPHA  + ")";
const waterText = "rgba(42,143,189," + STAT_ALPHA + ")";
const earthText = "rgba(168,120,72," + (STAT_ALPHA + .2) + ")";
const airText = "rgba(201,255,227," + STAT_ALPHA + ")";
const balanceText = "rgba(30,30,30," + (STAT_ALPHA) +  ")";
const DEFAULT_ANIM_COLOR = "rgba(100,100,100,1.0)";