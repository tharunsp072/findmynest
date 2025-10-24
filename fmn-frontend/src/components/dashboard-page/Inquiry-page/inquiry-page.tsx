import { Component, h, Prop, State } from '@stencil/core';
import { Booking, BookingStatus } from '../../../models/interfaces';

@Component({
  tag: 'inquiry-page',
  styleUrl: './inquiry-page.css',
  shadow: true,
})
export class InquiryPage {
  @State() bookings: Booking[] = [];
  // @State() bookingStatus : string;

  @Prop() role: string;

  async componentWillLoad() {
    console.log('role from user', this.role);
    try {
      const token = localStorage.getItem('token');

      const profileId = localStorage.getItem('profileId');
      let url = '';
      if (this.role === 'OWNER') {
        url = `http://localhost:8080/bookings/owners/findBookings/${profileId}`;
      } else {
        url = `http://localhost:8080/bookings/tenants/findBookings/${profileId}`;
      }
      const response = await fetch(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) throw new Error('Network error');
      const data = await response.json();
      console.log(data);
      this.bookings = data;
    } catch (err) {
      console.error(err);
    }
  }

  async handleBookingStatus(status: string, bookingId: number) {
    try {
      const token = localStorage.getItem('token');
      const ownerId = localStorage.getItem('profileId');
      const response = await fetch(`http://localhost:8080/bookings/owners/${ownerId}/updateBooking/${bookingId}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/plain',
        },
        body: status.toUpperCase(),
      });
      this.componentWillLoad();
      if (!response.ok) throw new Error('Network establishment error');
    } catch (err) {
      console.error(err);
    }
  }

  async handleDeleteBooking(bookindId: number) {
    try {
      const token = localStorage.getItem('token');
      const ownerId = localStorage.getItem('profileId');
      const response = await fetch(`http://localhost:8080/bookings/owners/${ownerId}/deleteBooking/${bookindId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error('Network Error');
      } else {
        this.componentWillLoad();
      }
    } catch (err) {
      console.error(err);
    }
  }
  renderOwnerProfile() {
    console.log(this.bookings);
    return (
      <div class="owner-property-inquiries">
        <ul class="bookings-list">
          {this.bookings.map(booking => (
            <li class="single-booking-data" key={booking.bookingId}>
              <img src={`/assets/${booking.property.imgUrl}`} alt="no image" class="image" />
              <div class="booking-data">
                <p>Title:{booking.property?.title ?? 'N/A'}</p>
                <p>Sqft:{booking.property?.carpetArea ?? 'N/A'}</p>
                <p>Rent:{booking.property?.price ?? 'N/A'}</p>

                <p>Furnished Status:{booking.property?.furnishedStatus ?? 'N/A'}</p>
                <p>Username: {booking.tenant?.username ?? 'N/A'}</p>
                <p>Contact Number:{booking.tenant?.contact_number ?? 'N/A'}</p>
              </div>

              {booking.status === BookingStatus.PENDING && (
                <div class={`btn-container ${booking.status}`}>
                  <button class="confirm-btn btn" onClick={() => this.handleBookingStatus('Confirmed', booking.bookingId)}>
                    Confirm
                  </button>

                  <button class={`cancel-btn btn ${booking.status}-btn`} onClick={() => this.handleBookingStatus('Cancelled', booking.bookingId)}>
                    Cancel
                  </button>
                </div>
              )}
              {booking.status === BookingStatus.CANCELLED && (
                <div class="cancel-booking">
                  <p>Booking Cancelled</p>
                </div>
              )}
              {booking.status === BookingStatus.CONFIRMED && (
                <div class="confirm-booking">
                  <p>Booking Confirmed</p>
                </div>
              )}

              <button class="delete-button" onClick={() => this.handleDeleteBooking(booking.bookingId)}>
                <span>❌</span>
              </button>
            </li>
          ))}
        </ul>
      </div>
    );
  }

  renderTenantProfile() {
    return (
      <div class="tenant-property-inquiries">
        <ul class="booking-list"></ul>
      </div>
    );
  }
  render() {
    return (
      <div>
        <h1>Inquiries</h1>
        {/* <div class="property-inquiry"> */}
        {this.role === 'OWNER' && this.renderOwnerProfile()}
        {/* </div> */}
      </div>
    );
  }
}
