export interface Property {
    propertyId : string;
    title:string;
    description : string;
    price : number;
    address : string;
    carpetArea : number;
    ageOfBuilding : number;
    availableStatus :string;
    furnishedStatus : string;
    imgUrl : string;
    booked?:boolean;
}