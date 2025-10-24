import { Component, h, Prop, State, Event, EventEmitter, Method } from '@stencil/core';
import { Booking, Property } from '../../../models/interfaces';

@Component({
  tag: 'my-rentals',
  styleUrl: './my-rentals.css',
  shadow: true,
})
export class MyRentals {
  @State() rentedProperties: Property[] = [];

  async componentWillLoad() {
    try {
      const token = localStorage.getItem('token');
      const tenantId = localStorage.getItem('profileId');
      const response = await fetch(`http://localhost:8080/bookings/tenants/findConfirmedProperties/${tenantId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) throw new Error(`Failed to fetch rented Properties`);
      const bookings: Booking[] = await response.json();
      console.log('Rented properties:', bookings);
      this.rentedProperties = bookings.map(booking => ({
        ...booking.property,
        booking: {
          bookingId: booking.bookingId,
          tenant: booking.tenant,
          property: booking.property,
          status: booking.status,
          endDate: booking.endDate,
          startDate: booking.startDate,
        },
      }));
    } catch (err) {
      console.error(`Error fetching rented Properties`, err);
    }
  }
  render() {
    return (
      <div class="layout">
        {this.rentedProperties.length > 0 ? (
          <div>
            {' '}
           
            <property-listing source="tenant" listingProperties={this.rentedProperties}></property-listing>
          </div>
        ) : (
          <div>
            <p>You havent Rented any properties yet</p>
            <p>Find your nest by clicking the Button below</p>
            <button>Find Nest</button>
          </div>
        )}
      </div>
    );
  }
}
