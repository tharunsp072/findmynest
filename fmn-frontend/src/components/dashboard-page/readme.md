# dashboard-page



<!-- Auto Generated Below -->


## Dependencies

### Depends on

- [app-navbar](../app-navbar)
- [property-listing](property-listing)
- [owner-properties](owner-properties)
- [add-property-form](add-property-form)
- [tenant-favorites](tenant-favorites)
- [user-profile](user-profile)
- [payments-page](payments-page)
- [inquiry-page](Inquiry-page)

### Graph
```mermaid
graph TD;
  dashboard-page --> app-navbar
  dashboard-page --> property-listing
  dashboard-page --> owner-properties
  dashboard-page --> add-property-form
  dashboard-page --> tenant-favorites
  dashboard-page --> user-profile
  dashboard-page --> payments-page
  dashboard-page --> inquiry-page
  property-listing --> property-card
  property-card --> inquiry-form
  owner-properties --> property-listing
  owner-properties --> add-property-form
  style dashboard-page fill:#f9f,stroke:#333,stroke-width:4px
```

----------------------------------------------

*Built with [StencilJS](https://stenciljs.com/)*
