import { Component, h, Prop, Event, EventEmitter, State } from '@stencil/core';
import { Property } from '../../../../models/interfaces';

@Component({
  tag: 'property-card',
  styleUrl: 'property-card.css',
  shadow: true,
})
export class PropertyCard {
  @Prop() propertys!: Property;
  @Prop() role: string;
  @Prop() bookingStatus: boolean;
  @Prop() sourceCard?: string;
  @State() showInquiryForm: boolean = false;
  @State() booked: boolean = false;
  @State() favorite: boolean = false;
  @State() showCancelConfirm: boolean = false;

  @Event() booking: EventEmitter<{ booked: boolean; property: Property }>;
  @Event() addToFavorites: EventEmitter<{ favorite: boolean; property: Property }>;
  @Event() cancelBooking: EventEmitter<{ cancel: boolean; property: Property }>;
  handleBooking = (e: Event) => {
    e.preventDefault();
    console.log('Booking event triggered for:', this.propertys);
    this.booked = !this.booked;
    this.booking.emit({ booked: this.booked, property: this.propertys });
  };

  handleFavorites = (e: Event) => {
    e.preventDefault();
    this.favorite = !this.favorite;
    this.addToFavorites.emit({ favorite: this.favorite, property: this.propertys });
  };

  componentWillLoad() {
    console.log(this.role);
    console.log(this.propertys);
  }

  handleShowInquiry() {
    this.showInquiryForm = !this.showInquiryForm;
  }

  handleCancelBooking() {
    this.showCancelConfirm = true;
  }

  confirmCancelBooking() {
    this.cancelBooking.emit({ cancel: true, property: this.propertys });
    this.showCancelConfirm = false;
  }

  closeCancelDialog() {
    this.showCancelConfirm = false;
  }
  renderTenantProperties() {
    return (
      <div>
        <h1>My NESTS</h1>
        <div class="tenant-rental-card">
          <img class="property-image" src={`/assets/${this.propertys.imgUrl}`} alt={this.propertys.title} />

          <div class="property-content">
            <h2 class="property-title">{this.propertys.title}</h2>
            <p class="property-address">{this.propertys.address}</p>

            <div class="property-details">
              <p>
                <strong>Area:</strong> {this.propertys.carpetArea} sq.ft
              </p>
              <p>
                <strong>Furnished:</strong> {this.propertys.furnishedStatus}
              </p>
              <p>
                <strong>Age:</strong> {this.propertys.ageOfBuilding} yrs
              </p>
              <p>
                <strong>Rent:</strong> ₹{this.propertys.price}
              </p>
              <p>
                <strong>Duration:</strong> {this.propertys.booking.startDate} → {this.propertys.booking.endDate}
              </p>
            </div>

            <div class="property-status">
              <span class={`status-badge ${this.propertys.status.toLowerCase()}`}>{this.propertys.status}</span>
            </div>
            <div class="tenant-actions">
              <button class="pay-btn">Pay Rent</button>
              <button class="cancel-btn" onClick={() => this.handleCancelBooking()}>
                Cancel Booking
              </button>
              <button class="view-btn">View Payments</button>
              {this.renderCancelDialog()}
            </div>
          </div>
        </div>
      </div>
    );
  }

  renderCancelDialog() {
    if (!this.showCancelConfirm) return null;

    return (
      <div class="cancel-dialog">
        <div class="dialog-content">
          <p>Are you sure you want to cancel this booking?</p>
          <div class="dialog-buttons">
            <button onClick={() => this.confirmCancelBooking()}>Yes</button>
            <button onClick={() => this.closeCancelDialog()}>No</button>
          </div>
        </div>
      </div>
    );
  }

  render() {
    return (
      <div>
        {this.sourceCard !== 'tenant' ? (
          <div class="property-card">
            <div class="property-info">
              <img src={`/assets/${this.propertys.imgUrl}`} alt={this.propertys.title} />
              <div class="info">
                <div class="desc-heart">
                  <p>{this.propertys.description}</p>
                  {this.role !== 'owner' && (
                    <button onClick={this.handleFavorites} class={`fav-btn ${this.favorite ? 'favorited' : ''}`}>
                      {this.favorite ? '❤️' : '🤍'}
                    </button>
                  )}
                </div>

                <div class="size-price">
                  <p>{this.propertys.carpetArea} sq.ft</p>
                  <p>₹{this.propertys.price}</p>
                </div>
                <p>{this.propertys.address}</p>
                <p>
                  {this.propertys.status.substring(0, 1)}
                  {this.propertys.status.substring(1, this.propertys.status.length).toLowerCase()}
                </p>

                <div class="book-And-Fav">
                  {/* Book button */}
                  {this.role !== 'owner' && (
                    <div class="book-inquiry-btns">
                      <button onClick={this.handleBooking} class={`book-btn ${this.booked ? 'booked' : ''}`}>
                        {this.bookingStatus ? 'Booked' : 'Book'}
                      </button>
                      {/* Favorite button */}
                      <button onClick={() => this.handleShowInquiry()}>Inquiry</button>
                      {this.showInquiryForm && (
                        <inquiry-form showForm={this.showInquiryForm} onCloseForm={() => (this.showInquiryForm = false)} propertyId={this.propertys.propertyId}></inquiry-form>
                      )}
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        ) : (
          this.renderTenantProperties()
        )}
      </div>
    );
  }
}
