<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
    <w:wordDocument
   xmlns:w="http://schemas.microsoft.com/office/word/2003/wordml"
   xmlns:v="urn:schemas-microsoft-com:vml"
   xmlns:o="urn:schemas-microsoft-com:office:office"
   xml:space="preserve">
  <w:body>
      <w:pict>
         <v:shape id="_x0000_i1025" type="_x0000_t75"
               style="width:48.75pt;height:38.25pt">
            <v:imagedata src="C:\Users\alexander\IdeaProjects\logreader\xsl\siblion_logo.gif"
                  o:title="FolderN"/>
           </v:shape>
      </w:pict>
      <w:p>
          <w:r><w:t>Log messages</w:t></w:r>
          <w:r><w:t><xsl:value-of select="Title"/></w:t></w:r>
      </w:p>
      <w:b/>
  	<xsl:for-each select="/logMessages/logMessage">
	<w:p>
      <w:r>
        <w:t><xsl:value-of select="date"/></w:t>
      </w:r>
    </w:p>
		<w:p>
      <w:r>
        <w:t><xsl:value-of select="message"/></w:t>
      </w:r>
    </w:p>
		<w:p>
    </w:p>
	</xsl:for-each>
  </w:body>
</w:wordDocument>
</xsl:template>
</xsl:stylesheet>