# dashboard-page



<!-- Auto Generated Below -->


## Dependencies

### Depends on

- [app-navbar](../app-navbar)
- [property-listing](property-listing)
- [owner-properties](owner-properties)
- [add-property-form](add-property-form)

### Graph
```mermaid
graph TD;
  dashboard-page --> app-navbar
  dashboard-page --> property-listing
  dashboard-page --> owner-properties
  dashboard-page --> add-property-form
  property-listing --> property-card
  owner-properties --> property-listing
  owner-properties --> add-property-form
  style dashboard-page fill:#f9f,stroke:#333,stroke-width:4px
```

----------------------------------------------

*Built with [StencilJS](https://stenciljs.com/)*
