//We assume that BACK will have a TID of 0 and the 5 elements will fill
//TIDs 1-5 in an agreed upon order that is based on how neat josh
//currently thinks they are.
class CardRepo{
	
	let BACK_TID = 0;
	
	let objects = new Map();
	
	static getCard(TID){
		return objects.get(TID);
	}
	
	static getBack(){
		return objects.get(BACK_TID);
	}
	
	static getElement(elementType){
		return objects.get(elementType);
	}
	
}