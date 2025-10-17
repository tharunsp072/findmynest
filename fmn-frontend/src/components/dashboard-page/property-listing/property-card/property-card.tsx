import { Component, h, Prop, Event, EventEmitter, State } from '@stencil/core';
import { Property } from '../property';


@Component({
  tag: 'property-card',
  styleUrl: 'property-card.css',
  shadow: true,
})
export class PropertyCard {
  @Prop() propertys!: Property;
  @Prop() role: string;
  @Prop() bookingStatus : boolean;

  @State() showInquiryForm: boolean = false;
  @State() booked: boolean = false;
  @State() favorite: boolean = false;

  @Event() booking: EventEmitter<{ booked: boolean; property: Property }>;
  @Event() addToFavorites: EventEmitter<{ favorite: boolean; property: Property }>;

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
  render() {
    return (
      <div class="property-card">
        <div class="property-info">
          <img src={`./assets/${this.propertys.imgUrl}`} alt={this.propertys.title} />
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
            <p>{this.propertys.availableStatus}</p>

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
    );
  }
}
