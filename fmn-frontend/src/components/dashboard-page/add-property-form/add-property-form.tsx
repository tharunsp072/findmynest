  import { Component, h, Prop, State, Event, EventEmitter } from '@stencil/core';
  import { AvailableStatus, Property } from '../../../models/interfaces';

  @Component({
    tag: 'add-property-form',
    styleUrl: 'add-property-form.css',
    shadow: true,
  })
  export class AddPropertyForm {
    @Prop() source: string;
    @Prop() user: number;
    @State() propertyForm: Property = {
      propertyId: '',
      title: '',
      description: '',
      price: 0,
      address: '',
      carpetArea: 0,
      ageOfBuilding: 0,
      furnishedStatus: '',
      imgUrl: '',
      booked: false,
      status: AvailableStatus.AVAILABLE,
    };

    async handleAdd(e: Event) {
      e.preventDefault();

      try {
        const token = localStorage.getItem('token');
        console.log('Sending JWT:', token);

        const response = await fetch(`http://localhost:8080/owners/listProperties/${this.user}`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(this.propertyForm),
        });

        window.location.href = "/dashboard/my-properties";
        if (!response.ok) {
          console.warn(`Network error ${response.status}`);
        } else {
          console.log('added successfully');
        }
      } catch (err) {
        console.error('Error Adding new property', err);
      }
    }

    handleInputChange(e: Event) {
      const target = e.target as HTMLInputElement | HTMLSelectElement;
      const name = target.name;
      const value = target.value; // always a string from select/input
      this.propertyForm = {
        ...this.propertyForm,
        [name]: value,
      };
    }

    handleFileChange(e: Event) {
      const target = e.target as HTMLInputElement;
      if (target.files && target.files[0]) {
        this.propertyForm.imgUrl = target.files[0].name;
      }
    }

    render() {
      return (
        <div class="layout">
          <form onSubmit={e => this.handleAdd(e)} class="add-form" action="/my-listings">
            <h1>Add New Property</h1>

            <div>
              <label htmlFor="title">Title</label>
              <input type="text" name="title" id="title" value={this.propertyForm.title} onInput={e => this.handleInputChange(e)} />
            </div>

            <div>
              <label htmlFor="description">Description</label>
              <input type="text" name="description" id="description" value={this.propertyForm.description} onInput={e => this.handleInputChange(e)} />
            </div>

            <div>
              <label htmlFor="price">Price</label>
              <input type="number" name="price" id="price" value={this.propertyForm.price} onInput={e => this.handleInputChange(e)} />
            </div>

            <div>
              <label htmlFor="address">Address</label>
              <input type="text" name="address" id="address" value={this.propertyForm.address} onInput={e => this.handleInputChange(e)} />
            </div>

            <select name="status" onInput={e => this.handleInputChange(e)}>
              <option value={AvailableStatus.AVAILABLE} selected={this.propertyForm.status === AvailableStatus.AVAILABLE}>
                AVAILABLE
              </option>
              <option value={AvailableStatus.BOOKED} selected={this.propertyForm.status === AvailableStatus.BOOKED}>
                BOOKED
              </option>
            </select>

            <div>
              <label htmlFor="carpetArea">Carpet Area</label>
              <input type="number" name="carpetArea" id="carpetArea" value={this.propertyForm.carpetArea} onInput={e => this.handleInputChange(e)} />
            </div>

            <div>
              <label htmlFor="ageOfBuilding">Age of Building</label>
              <input type="number" name="ageOfBuilding" id="ageOfBuilding" value={this.propertyForm.ageOfBuilding} onInput={e => this.handleInputChange(e)} />
            </div>

            <div>
              <label htmlFor="furnishedStatus">Furnished Status</label>
              <input type="text" name="furnishedStatus" id="furnishedStatus" value={this.propertyForm.furnishedStatus} onInput={e => this.handleInputChange(e)} />
            </div>

            <div>
              <label htmlFor="image">Property Image</label>
              <input type="file" id="image" name="imgUrl" onChange={e => this.handleFileChange(e)} />
            </div>

            <div class="submit-container">
                <button type="submit" >Submit</button>
            </div>
          </form>
        </div>
      );
    }
  }
