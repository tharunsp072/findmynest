import { Component, h, State, Prop, Watch } from '@stencil/core';
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
  // @State() selectedPaymentMode : string = '';
  @State() bookProperty: boolean;

  @Prop() listingProperties?: Property[] = [];
  @Prop() source?: string;
  @Prop() user?: number;

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

  @Watch('listingProperties')
  listingPropertiesChanged(newValue: Property[]) {
    this.properties = newValue || [];
  }

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

  handleBookingEvent(e: CustomEvent<{ booked: boolean; property: Property }>) {
    const { booked, property } = e.detail;

    if (booked) {
      this.selectedProperty = property;
      this.showBookingDialog = true;
    }
  }

  closeDialog() {
    this.showBookingDialog = false;
    this.selectedProperty = null;
    this.startDate = '';
    this.endDate = '';
  }

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
      // alert('Property booked successfully!');

      this.closeDialog();
    } catch (err) {
      console.error(err);
      alert('Failed to book property');
    }
  }

  handleCancelBookingEvent(e: CustomEvent<{ cancel: boolean; property: Property }>) {
    const propertyId = e.detail.property.propertyId;

    if (e.detail.cancel) {
      this.handleDeleteProperty(new Event(''), propertyId, this.user);
    }
  }

  handleBookingStatus(propertyId: string) {
    this.properties = this.properties.map(p => (p.propertyId === propertyId ? { ...p, booked: true } : p));
  }
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
            Start Date:
            <input type="date" value={this.startDate} onInput={(e: any) => (this.startDate = e.target.value)} />
          </label>
          <label>
            End Date:
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
