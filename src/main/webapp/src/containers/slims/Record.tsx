export interface Record {
    tableName: string;
    pk: number;
    columns: Array<Column>;
    links: Array<Link>
}

export interface Column {
    datatype: string;
    name: string;
    title: string;
    hidden: boolean;
    position: number;
    value: any;
    displayValue?: string;
    displayField?: string;
    foreignDisplayColumn?: string;
    foreignTable?: string;
}

interface Link {
    // The field that has a follow
    rel: string;
    href: string;
}

export function value(record: Record, columnName: string) {
    let v = record.columns.find(c => c.name === columnName)
    if (v) {
        if (v.displayValue) {
            return nullOrEmpty(v.displayValue)
        } else if(v.datatype === "DATE") {
            return new Date(v.value).toString();
        } else {
            return nullOrEmpty(v.value)
        }
    } else {
        return ""
    }
}

function nullOrEmpty(v: string) {
    if (v) {
        return v;
    } else {
        return "";
    }
}