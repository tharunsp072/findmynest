import { Component, h, Prop, State, Event, EventEmitter, Method } from '@stencil/core';
import { Booking, Payment, PaymentStatus, Property } from '../../../models/interfaces';
import { json } from 'stream/consumers';

@Component({
  tag: 'my-rentals',
  styleUrl: './my-rentals.css',
  shadow: true,
})
export class MyRentals {
  @State() rentedProperties: Property[] = [];
  @State() showPaymentDialog: boolean = false;
  @State() selectedProperty: Property;
  @State() paymentMode: string = 'UPI';
  @State() remarks: string = '';
  @State() makePayment: Payment = {
    paymentDate: new Date(),
    dueDate: new Date(),
    price: 0,
    paidAmount: 0,
    paymentMode: '',
    paymentStatus: PaymentStatus.PENDING,
    monthNumber: new Date().getMonth() + 1,
    booking: null,
    tenant: null,
  };

  handlePayRent(e: CustomEvent<{ property: Property; pay: boolean }>) {
    const { property } = e.detail;
    console.log('Handle rent payment for myrental:', property);
    this.showPaymentDialog = true;
    console.log(this.showPaymentDialog);
    this.selectedProperty = property;
  }

  async handlePayment() {
    if (!this.selectedProperty) return;

    const tenantId = localStorage.getItem('profileId');
    const token = localStorage.getItem('token');

    const payment: Payment = {
      paymentDate: new Date(),
      dueDate: new Date(), // optional, can be property-based
      price: this.selectedProperty.price,
      paidAmount: this.selectedProperty.price, 
      paymentMode: this.paymentMode,
      paymentStatus: PaymentStatus.PAID,
      monthNumber: new Date().getMonth() + 1,
      booking: this.selectedProperty.booking,
      tenant: this.selectedProperty.booking.tenant,
    };

    const response = await fetch(`http://localhost:8080/payments/process/${this.selectedProperty.booking.bookingId}`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payment),
    });

    if (response.ok) {
      // alert('Payment successful!');
      this.showPaymentDialog = false;
    } else {
      const error = await response.text();
      // alert('Payment failed: ' + error);
    }
  }

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
            <property-listing
              source="tenant"
              listingProperties={this.rentedProperties}
              onPayRent={(e: CustomEvent<{ property: Property; pay: boolean }>) => this.handlePayRent(e)}
            ></property-listing>
            {this.showPaymentDialog && (
              <div class="payment-dialog">
                <div class="payment-content">
                  <h2>Rent Payment</h2>
                  <p>
                    <strong>Property:</strong> {this.selectedProperty?.title || 'Selected property'}
                  </p>
                  <p>
                    <strong>Rent Amount:</strong> ₹{this.selectedProperty?.price || '---'}
                  </p>

                  <label>
                    Payment Method:
                    <select onInput={(e: any) => (this.paymentMode = e.target.value)}>
                      <option>UPI</option>
                      <option>Credit/Debit Card</option>
                      <option>Net Banking</option>
                    </select>
                  </label>

                  <label>
                    Remarks:
                    <input type="text" placeholder="Optional note" value={this.remarks} onInput={(e: any) => (this.remarks = e.target.value)} />
                  </label>

                  <div class="dialog-buttons">
                    <button class="confirm-btn" onClick={() => this.handlePayment()}>
                      Confirm Payment
                    </button>
                    <button class="cancel-btn" onClick={() => (this.showPaymentDialog = false)}>
                      Cancel
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        ) : (
          <div>
            <p>You havent Rented any properties yet</p>
            <p>Find your nest by clicking the Button below</p>
            <stencil-route-link url="/dashboard/home">
              <button>Find Nest</button>
            </stencil-route-link>
          </div>
        )}
      </div>
    );
  }
}
