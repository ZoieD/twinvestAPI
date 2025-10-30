within;

package TwinvestWindFMUs
  // 1) WindIn: pass-through for scenario input (u -> y)
  model WindIn
    Modelica.Blocks.Interfaces.RealInput u "scenario wind input";
    Modelica.Blocks.Interfaces.RealOutput y "wind out";
  equation
    y = u;
  end WindIn;

  // 2) PriceIn: pass-through for scenario price (u -> y)
  model PriceIn
    Modelica.Blocks.Interfaces.RealInput u "scenario price input";
    Modelica.Blocks.Interfaces.RealOutput y "price out";
  equation
    y = u;
  end PriceIn;

  // 3) WakeGain: simple attenuation y = k * u
  model WakeGain
    parameter Real k(min=0, max=1) = 0.9 "attenuation factor";
    Modelica.Blocks.Interfaces.RealInput u "v_in";
    Modelica.Blocks.Interfaces.RealOutput y "v_down";
  equation
    y = k * u;
  end WakeGain;

  // 4) TurbinePoly: piecewise power curve
  model TurbinePoly
    parameter Real vCutIn   = 3.0 "m/s";
    parameter Real vRated   = 12.0 "m/s";
    parameter Real vCutOut  = 25.0 "m/s";
    parameter Real pRatedKw = 3500.0 "kW";
    Modelica.Blocks.Interfaces.RealInput  v "wind m/s";
    Modelica.Blocks.Interfaces.RealOutput P "power kW";
  protected
    Real x;
  equation
    if v < vCutIn or v >= vCutOut then
      P = 0;
    elseif v >= vRated then
      P = pRatedKw;
    else
      x = (v - vCutIn) / (vRated - vCutIn);
      P = pRatedKw * x^3;
    end if;
  end TurbinePoly;

  // 5) Sum2: y = u1 + u2
  model Sum2
    Modelica.Blocks.Interfaces.RealInput u1 "kW";
    Modelica.Blocks.Interfaces.RealInput u2 "kW";
    Modelica.Blocks.Interfaces.RealOutput y "kW";
  equation
    y = u1 + u2;
  end Sum2;

end TwinvestWindFMUs;
