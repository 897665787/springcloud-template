        - id: gateway-{fuwu}-{apipath}
          uri: lb://{fuwu}
          predicates:
            - Path={apipath}