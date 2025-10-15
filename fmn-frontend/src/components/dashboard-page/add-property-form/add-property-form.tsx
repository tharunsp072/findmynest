import { Component, h, Prop, State, Event, EventEmitter } from '@stencil/core';

@Component({
  tag: 'add-property-form',
  styleUrl: 'add-property-form.css',
  shadow: true,
})
export class AddPropertyForm {
  @Prop() source: string;
  @Prop() user: number;
  @State() propertyForm = {
    title: '',
    description: '',
    price: 0,
    address: '',
    status: '',
    carpetArea: 0,
    ageOfBuilding: 0,
    furnishedStatus: '',
    imgUrl: '',
  };

  @Event() pageRender: EventEmitter<string>;
  handleNavChange(activeMenu: string) {
    this.pageRender.emit(activeMenu);
  }

  async handleAdd(e: Event) {
    e.preventDefault();
    this.handleNavChange('my-properties');

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
    const target = e.target as HTMLInputElement;
    const name = target.name;
    const value = target.type === 'number' ? Number(target.value) : target.value;

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
        <form onSubmit={e => this.handleAdd(e)} class="add-form" action="/my-listings" enctype="multipart/form-data">
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

          <div>
            <label htmlFor="status">Status</label>
            <input type="text" name="status" id="status" value={this.propertyForm.status} onInput={e => this.handleInputChange(e)} />
          </div>

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
            <button type="submit">Submit</button>
          </div>
        </form>
      </div>
    );
  }
}
