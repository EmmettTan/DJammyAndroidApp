LIBRARY ieee;
USE ieee.std_logic_1164.all; 
USE ieee.std_logic_arith.all; 
USE ieee.std_logic_unsigned.all; 
ENTITY integration IS
   PORT (
		SW : IN STD_LOGIC_VECTOR(7 DOWNTO 0);
      KEY : IN STD_LOGIC_VECTOR(3 DOWNTO 0);
      CLOCK_50 : IN STD_LOGIC;
		LEDG : OUT STD_LOGIC_VECTOR(7 DOWNTO 0);
      DRAM_CLK, DRAM_CKE : OUT STD_LOGIC;
      DRAM_ADDR : OUT STD_LOGIC_VECTOR(11 DOWNTO 0);
      DRAM_BA_0, DRAM_BA_1 : BUFFER STD_LOGIC;
      DRAM_CS_N, DRAM_CAS_N, DRAM_RAS_N, DRAM_WE_N : OUT STD_LOGIC;
      DRAM_DQ : INOUT STD_LOGIC_VECTOR(15 DOWNTO 0);
      DRAM_UDQM, DRAM_LDQM : BUFFER STD_LOGIC;
		
		
		--SDCARD--
		SD_CMD, SD_DAT, SD_DAT3 : inout std_logic;
		SD_CLK : out std_logic;
		
		--SRAM--
		SRAM_DQ	:	INOUT	STD_LOGIC_VECTOR(15	downto	0);
		SRAM_ADDR	:	OUT	STD_LOGIC_VECTOR(17	downto	0);
		SRAM_LB_N	:	OUT	STD_LOGIC;
		SRAM_UB_N	:	OUT	STD_LOGIC;
		SRAM_CE_N	:	OUT	STD_LOGIC;
		SRAM_OE_N	:	OUT	STD_LOGIC;
		SRAM_WE_N	:	OUT	STD_LOGIC;
		
		--AUDIO--
		I2C_SDAT : inout std_logic;
		I2C_SCLK : out   std_logic;
		AUD_XCK:  out   std_logic; 
		CLOCK_27: in    std_logic;
		AUD_ADCDAT        : in    std_logic;             -- ADCDAT
		AUD_ADCLRCK       : in    std_logic;             -- ADCLRCK
      AUD_BCLK          : in    std_logic;             -- BCLK
      AUD_DACDAT        : out   std_logic;                                        -- DACDAT
      AUD_DACLRCK       : in    std_logic;

			OTG_INT1			: in std_logic;
			OTG_DATA			: inout STD_LOGIC_VECTOR(15 downto 0);
			OTG_RST_N		: out std_LOGIC;
			OTG_ADDR			: out STD_LOGIC_VECTOR(1 DOWNTO 0);
			OTG_CS_N			: OUT STD_LOGIC;
			OTG_RD_N			: OUT STD_LOGIC;
			OTG_WR_N			: OUT STD_LOGIC;
			OTG_INT0			: IN STD_LOGIC;
			GPIO_0			: out STD_LOGIC_vector(35 DOWNTO 0)
			);
   END integration;

ARCHITECTURE Structure OF integration IS
   COMPONENT AndroidPlatformQsys PORT (
				clk_clk                  : in    std_logic;             -- clk
            reset_reset_n            : in    std_logic;             -- reset_n
            sdram_wire_addr          : out   std_logic_vector(11 downto 0);                    -- addr
            sdram_wire_ba            : out   std_logic_vector(1 downto 0);                     -- ba
            sdram_wire_cas_n         : out   std_logic;                                        -- cas_n
            sdram_wire_cke           : out   std_logic;                                        -- cke
            sdram_wire_cs_n          : out   std_logic;                                        -- cs_n
            sdram_wire_dq            : inout std_logic_vector(15 downto 0); -- dq
            sdram_wire_dqm           : out   std_logic_vector(1 downto 0);                     -- dqm
            sdram_wire_ras_n         : out   std_logic;                                        -- ras_n
            sdram_wire_we_n          : out   std_logic;                                        -- we_n
            sdram_clk_clk            : out   std_logic;                                        -- clk
            switches_export          : in    std_logic_vector(7 downto 0);							  -- export
            leds_export              : out   std_logic_vector(7 downto 0);                     -- export
            pushbuttons_export       : in    std_logic_vector(3 downto 0); 							  -- export
            sd_card_ports_b_SD_cmd   : inout std_logic;                     		              -- b_SD_cmd
            sd_card_ports_b_SD_dat   : inout std_logic;                     		              -- b_SD_dat
            sd_card_ports_b_SD_dat3  : inout std_logic;                     			 	        -- b_SD_dat3
            sd_card_ports_o_SD_clock : out   std_logic;
				
				audio_0_external_ADCDAT        : in    std_logic;             -- ADCDAT
				audio_0_external_ADCLRCK       : in    std_logic;             -- ADCLRCK
				audio_0_external_BCLK          : in    std_logic;             -- BCLK
				audio_0_external_DACDAT        : out   std_logic;                                        -- DACDAT
				audio_0_external_DACLRCK       : in    std_logic;             -- DACLRCK
				audio_and_video_interface_SDAT : inout std_logic;             -- SDAT
				audio_and_video_interface_SCLK : out   std_logic;                                         -- SCLK
				clocks_audio_clk_clk           : out   std_logic;                                        -- clk
				clocks_clk_in_secondary_clk    : in    std_logic;            -- clk
				
				usb_controller_INT1            : in    std_logic;             -- INT1
            usb_controller_DATA            : inout std_logic_vector(15 downto 0); -- DATA
            usb_controller_RST_N           : out   std_logic;                                        -- RST_N
            usb_controller_ADDR            : out   std_logic_vector(1 downto 0);                     -- ADDR
            usb_controller_CS_N            : out   std_logic;                                        -- CS_N
            usb_controller_RD_N            : out   std_logic;                                        -- RD_N
            usb_controller_WR_N            : out   std_logic;                                        -- WR_N
            usb_controller_INT0            : in    std_logic;            -- INT0	
				de2_pins_export                : out   std_logic_vector(7 downto 0)
        );
   END COMPONENT;

   SIGNAL DQM : STD_LOGIC_VECTOR(1 DOWNTO 0);
   SIGNAL BA : STD_LOGIC_VECTOR(1 DOWNTO 0);
	SIGNAL de2_pins: STD_LOGIC_VECTOR(7 downto 0);
   BEGIN
      DRAM_BA_0 <= BA(0);
      DRAM_BA_1 <= BA(1);
      DRAM_UDQM <= DQM(1);
      DRAM_LDQM <= DQM(0);
		GPIO_0(7 DOWNTO 0) <= DE2_pins(7 DOWNTO 0);
		
		NiosII: AndroidPlatformQsys PORT MAP(
			clk_clk => CLOCK_50,
			reset_reset_n => Key(0),
			sdram_clk_clk => DRAM_CLK,
			sdram_wire_addr => DRAM_ADDR,
         sdram_wire_ba => BA,
         sdram_wire_cas_n => DRAM_CAS_N,
         sdram_wire_cke => DRAM_CKE,
         sdram_wire_cs_n => DRAM_CS_N,
         sdram_wire_dq => DRAM_DQ,
         sdram_wire_dqm => DQM,
         sdram_wire_ras_n => DRAM_RAS_N,
         sdram_wire_we_n => DRAM_WE_N, 
			
			leds_export => LEDG,

         switches_export => SW,
			
			pushbuttons_export => KEY,
			
			sd_card_ports_b_SD_cmd => SD_CMD,
			sd_card_ports_b_SD_dat => SD_DAT,
			sd_card_ports_b_SD_dat3 => SD_DAT3,
			sd_card_ports_o_SD_clock => SD_CLK,
			
			audio_0_external_ADCDAT  =>  AUD_ADCDAT,
			audio_0_external_ADCLRCK => AUD_ADCLRCK,
			audio_0_external_BCLK    => AUD_BCLK,
			audio_0_external_DACDAT  => AUD_DACDAT,
			audio_0_external_DACLRCK => AUD_DACLRCK,
			audio_and_video_interface_SDAT => I2C_SDAT,
			audio_and_video_interface_SCLK => I2C_SCLK,
			clocks_audio_clk_clk    => AUD_XCK,       
			clocks_clk_in_secondary_clk => CLOCK_27,
			
			usb_controller_INT1            => OTG_INT1,            --            usb_controller.INT1
         usb_controller_DATA            => OTG_DATA,            --                          .DATA
         usb_controller_RST_N           => OTG_RST_N,           --                          .RST_N
         usb_controller_ADDR            => OTG_ADDR,            --                          .ADDR
         usb_controller_CS_N            => OTG_CS_N,            --                          .CS_N
         usb_controller_RD_N            => OTG_RD_N,            --                          .RD_N
         usb_controller_WR_N            => OTG_WR_N,            --                          .WR_N
         usb_controller_INT0            => OTG_INT0,   
			de2_pins_export                => de2_pins
			
			);
			
		

   END Structure;