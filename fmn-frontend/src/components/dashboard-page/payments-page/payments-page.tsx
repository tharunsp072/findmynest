import { Component, h, Prop, State } from '@stencil/core';

interface Payment {
  id: number;
  tenantName: string;
  propertyTitle: string;
  actualRent: number;
  paidRent: number;
  paymentDate: string;
}

@Component({
  tag: 'payments-page',
  styleUrl: 'payments-page.css',
  shadow: true,
})
export class PaymentsPage {
  @Prop() role: string;
  @Prop() userId: number; 


  @State() payments: Payment[] = [];
  @State() loading: boolean = true;
  @State() error: string = '';

  async componentWillLoad() {
    let url = '';

    if (this.role === 'owner') {
      url = `http://localhost:8080/payments/owner/${this.userId}`;
    } else if (this.role === 'tenant') {
      url = `http://localhost:8080/payments/tenant/${this.userId}`;
    }
    console.log(url);

    try {
      const token = localStorage.getItem('token');
      const res = await fetch(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        throw new Error(`Failed to fetch payments: ${res.status}`);
      }

      this.payments = await res.json();
    } catch (err) {
      console.error('Error fetching payments:', err);
      this.error = 'Could not load payment data.';
    } finally {
      this.loading = false;
    }
  }

  renderTable() {
    if (this.loading) return <p>Loading payment data...</p>;
    if (this.error) return <p class="error">{this.error}</p>;
    if (!this.payments.length) return <p>No payments found.</p>;

    return (
      <table class="payments-table">
        <thead>
          <tr>
            <th>Sl. No</th>
            <th>Tenant</th>
            <th>Property</th>
            <th>Actual Rent</th>
            <th>Paid Rent</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {this.payments.map((p, index) => (
            <tr>
              <td>{index + 1}</td>
              <td>{p.tenantName}</td>
              <td>{p.propertyTitle}</td>
              <td>₹{p.actualRent}</td>
              <td>₹{p.paidRent}</td>
              <td>{new Date(p.paymentDate).toLocaleDateString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  }

  render() {
    return (
      <div class="payment-page">
        <h1>{this.role === 'owner' ? 'Rent Received' : 'Payment History'}</h1>
        {this.renderTable()}
      </div>
    );
  }
}
