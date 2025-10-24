export interface Booking { 
    bookingId : number;
    tenant : TenantProfile;
    property : Property;
    status : BookingStatus;
    startDate : Date;
    endDate:Date;
} 

export enum BookingStatus {
    PENDING = 'PENDING',
    CANCELLED = 'CANCELLED',
    CONFIRMED = 'CONFIRMED'
}

export enum AvailableStatus { 
    AVAILABLE = 'AVAILABLE',
    BOOKED = 'BOOKED'
}

export interface Property {
  propertyId: string;
  title: string;
  description: string;
  price: number;
  address: string;
  carpetArea: number;
  ageOfBuilding: number;
  furnishedStatus: string;
  imgUrl: string;
  booked: boolean;
  status: AvailableStatus;
  booking? : Booking;
}

export interface TenantProfile { 
    tenantId :number;
    username : string;
    contact_number  :string;
    preferences : string;
    owner? : OwnerProfile;
    bookings? : Booking;
}

export interface OwnerProfile {
    ownerId : number;
    fullname : string;
    contact_number : number;
    address : string;
    total_revenue : string;
}


export interface Payment {
     paymentId? : number;
     paymentDate : Date;
     dueDate  : Date;
     price : number;
     paidAmount : number;
     paymentMode : string;
     paymentStatus : PaymentStatus;
     monthNumber : number;
     booking : Booking;
     tenant : TenantProfile;

}

export enum PaymentStatus {
     PENDING = "PENDING",
     PAID = "PAID"
}