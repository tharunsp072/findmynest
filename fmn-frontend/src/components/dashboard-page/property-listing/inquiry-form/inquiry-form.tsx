import { Component, h, Prop, State, Event, EventEmitter, Method } from '@stencil/core';

@Component({
  tag: 'inquiry-form',
  styleUrl: 'inquiry-form.css',
  shadow: true
})
export class InquiryForm {
  @Prop() showForm :boolean;
  @State() message : string='';
  @Prop() propertyId : string | number;

  @Event() closeForm : EventEmitter<void>;
  sendInquiry=async ()=>{
    try{
        const token = localStorage.getItem('token');
        const tenantId = Number(localStorage.getItem('profileId'));
        const propertyIdNumber = Number(this.propertyId);
         const response = await fetch(`http://localhost:8080/inquiry/tenant/${tenantId}/property/${propertyIdNumber}`, {
           method: 'POST',
           headers: {
             'Authorization': `Bearer ${token}`,
             'Content-Type': 'application/json',
           },
           body: JSON.stringify({
             message: this.message,
             status: 'PENDING',
             inquiryDate: new Date().toISOString(),
           }),
           mode: 'cors', 
           credentials: 'include',
         });

          if(!response.ok)throw new Error(`Http network error ${response.status}`);
      } catch (err){
        console.error(err);
    }
  }

  
  render() {
    return (
      <div>
        {this.showForm && (

          <div class="inquiry-modal">
            <textarea placeholder="Write your message" value={this.message} onInput={(e: any) => (this.message = e.target.value)}></textarea>
            <div class="actions">
              <button onClick={() => this.sendInquiry()}>Send</button>
              <button onClick={() => (this.closeForm.emit())}>Cancel</button>
            </div>
          </div>
        )}
      </div>
    );
  }
}