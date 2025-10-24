import { Component, h, State, Prop, Watch, Event, EventEmitter } from '@stencil/core';
import { Property } from '../../../models/interfaces';

@Component({
  tag: 'property-listing',
  styleUrl: 'property-listing.css',
  shadow: true,
})
export class PropertyListing {
  @State() properties: Property[] = [];
  @State() error: string;
  @State() selectedProperty: Property | null = null;
  @State() showBookingDialog: boolean = false;
  @State() startDate: string = '';
  @State() endDate: string = '';
  @State() bookProperty: boolean;

  @Prop() listingProperties?: Property[] = [];
  @Prop() source?: string;
  @Prop() user?: number;

  @Event() payRent: EventEmitter<{ property: Property; pay: boolean }>;

  // using the componentWillLoad() to assign the properties data based on the role
  async componentWillLoad() {
    if (this.source === 'owner') {
      this.properties = this.listingProperties?.length ? this.listingProperties : [];
    } else if (this.source === 'tenant') {
      this.properties = this.listingProperties?.length ? this.listingProperties : [];
    } else {
      this.properties = await this.fetchAllProperties();
      console.log(this.properties);
    }
  }

  // using the @watch decorator to caputre the changes made on the state listingproperties and
  // and then call the function listingPropertiesChanged to handle the changes in the list of properties
  // by dynamically updating it without running the componentWillLoad() everytime
  @Watch('listingProperties')
  listingPropertiesChanged(newValue: Property[]) {
    this.properties = newValue || [];
  }

  //fetching all the properties which are having the booking status
  //  as pending or cancelled and not confirmed to display in the home page
  fetchAllProperties = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('http://localhost:8080/properties/findNotConfirmed', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) throw new Error('Network response was not ok');
      return await response.json();
    } catch (err) {
      this.error = 'Error fetching properties';
      console.error(err);
      return [];
    }
  };

  //Handling the delete functionality given only to the owner on owner's listings only
  handleDeleteProperty = async (e: Event, propertyId: string, userId: number) => {
    if (!userId) {
      console.error('User ID is missing');
      return;
    }

    const oldProperties = [...this.properties];
    this.properties = this.properties.filter(p => p.propertyId !== propertyId);

    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/owners/deleteProperty/property/${propertyId}/user/${userId}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) {
        this.properties = oldProperties;
        throw new Error(`Failed to delete property. Status: ${response.status}`);
      }
      console.log(`Property ${propertyId} deleted successfully`);
    } catch (err) {
      console.error(err);
      this.error = 'Failed to delete property';
    }
  };

  //capturing the event from the property-card child to display the booking dialog module of the property which is to be booked
  handleBookingEvent(e: CustomEvent<{ booked: boolean; property: Property }>) {
    const { booked, property } = e.detail;

    if (booked) {
      this.selectedProperty = property;
      this.showBookingDialog = true;
    }
  }

  //closing the booking dialog module
  closeDialog() {
    this.showBookingDialog = false;
    this.selectedProperty = null;
    this.startDate = '';
    this.endDate = '';
  }

  //saving the booking informeation to the backend once the tenant confirms the booking date
  async confirmBooking() {
    if (!this.startDate || !this.endDate) {
      alert('Please select start and end dates');
      return;
    }

    if (!this.selectedProperty?.propertyId) {
      alert('Property ID is missing');
      return;
    }

    try {
      console.log(localStorage.getItem('token'));

      const token = localStorage.getItem('token');
      const tenantId = Number(localStorage.getItem('profileId'));
      const response = await fetch(`http://localhost:8080/bookings/saveBooking/${tenantId}/property/${this.selectedProperty.propertyId}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          startDate: this.startDate,
          endDate: this.endDate,
        }),
      });

      if (!response.ok) {
        throw new Error(`Failed to Book property. Status: ${response.status}`);
      } else {
        this.handleBookingStatus(this.selectedProperty.propertyId);
      }
      this.closeDialog();
    } catch (err) {
      console.error(err);
      alert('Failed to book property');
    }
  }

  //booking cancellation by tenant after being confirmed by owner
  async handleCancelBookingEvent(e: CustomEvent<{ cancel: boolean; property: Property }>) {
    const bookingId = e.detail.property.booking.bookingId;
    try {
      const token = localStorage.getItem('token');
      const tenantId = localStorage.getItem('profileId');
      const response = await fetch(`http://localhost:8080/bookings/tenants/${tenantId}/updateBooking/${bookingId}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) throw new Error(`${response.status}}`);
    } catch (err) {
      console.error(err);
    }
  }

  //updating the booking status to handle the interface
  handleBookingStatus(propertyId: string) {
    this.properties = this.properties.map(p => (p.propertyId === propertyId ? { ...p, booked: true } : p));
  }

  handlePayRentEvent(e: CustomEvent) {
    // if (this.payRent) {
    //   // only emit if parent is listening
    //   // console.log(e.detail);
    //   this.payRent.emit(e.detail);
    // }
  }

  //rendering the booking module in tenant profile once the tenant clicks on book button
  renderBookingDialog() {
    if (!this.showBookingDialog || !this.selectedProperty) return null;

    return (
      <div class="booking-dialog">
        <div class="dialog-content">
          <h3>Book Property</h3>
          <p>
            <b>{this.selectedProperty.title}</b>
          </p>
          <label>
            Start Date
            <input type="date" value={this.startDate} onInput={(e: any) => (this.startDate = e.target.value)} />
          </label>
          <label>
            End Date
            <input type="date" value={this.endDate} onInput={(e: any) => (this.endDate = e.target.value)} />
          </label>
          <div class="dialog-buttons">
            <button onClick={() => this.confirmBooking()}>Confirm</button>
            <button onClick={() => this.closeDialog()}>Cancel</button>
          </div>
        </div>
      </div>
    );
  }

  render() {
    return (
      <div class="property-listing">
        {this.properties.length === 0 ? (
          <p>No Properties Available</p>
        ) : (
          this.properties.map(property => (
            <div class={`${this.source}-property-item`}>
              <property-card
                role={this.source}
                sourceCard={this.source}
                propertys={property}
                onBooking={(e: CustomEvent<{ booked: boolean; property: Property }>) => this.handleBookingEvent(e)}
                onCancelBooking={(e: CustomEvent<{ cancel: boolean; property: Property }>) => this.handleCancelBookingEvent(e)}
                bookingStatus={property.booked}
                onPayRent={(e: CustomEvent) => this.handlePayRentEvent(e)}
              />

              {this.source === 'owner' && this.user && <button onClick={e => this.handleDeleteProperty(e, property.propertyId, this.user)}>❌</button>}
            </div>
          ))
        )}
        {this.error && <p>{this.error}</p>}
        {this.renderBookingDialog()}
      </div>
    );
  }
}
